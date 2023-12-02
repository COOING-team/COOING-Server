package com.example.cooing.domain.auth;



import com.example.cooing.domain.auth.jwt.JwtService;
import com.example.cooing.domain.auth.kakao.OAuthService;
import com.example.cooing.domain.auth.kakao.res.KakaoInfoResponse;
import com.example.cooing.global.entity.User;
import com.example.cooing.global.enums.OAuthProvider;
import com.example.cooing.global.enums.Role;
import com.example.cooing.global.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final OAuthService oAuthService;
    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Transactional
    public LoginResponseDto login(String authorizationCode){
        String accessToken = oAuthService.requestAccessToken(authorizationCode);
        KakaoInfoResponse kakaoInfoResponse =oAuthService.requestOauthInfo(accessToken);
        String email=kakaoInfoResponse.getKakaoAccount().getEmail();
        System.out.println(email);
        User user = userRepository.findByEmail(email).orElseGet(
                ()->saveUser(kakaoInfoResponse)
        );
        String serviceAccessToken= jwtService.createAccessToken(email);


        return LoginResponseDto.builder()
                .accessToken(serviceAccessToken)
                .userId(user.getId())
                .role(user.getRole())
                .userName(user.getName())
                .build();
    }

    @Transactional
    public User saveUser(KakaoInfoResponse kakaoInfoResponse){

        User member = User.builder()
                .oAuthProvider(OAuthProvider.KAKAO)
                .email(kakaoInfoResponse.getKakaoAccount().getEmail())
                .role(Role.USER)
                .name(kakaoInfoResponse.getKakaoAccount().getProfile().getNickname())
                .build();
        userRepository.saveAndFlush(member);
        return  member;

    }

    @Transactional
    public MyInfoResponseDto getMyInfo(CustomUserDetails userDetails){
        User user = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() ->new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        //Todo 프로필 이미지 넣어야함
        return  MyInfoResponseDto.builder()
                .name(userDetails.getUsername())
                .email(user.getEmail())
//                .followingNum(user.getFollows().size())
                .profilePicUrl(user.getProfilePicUrl())
                .build();
    }

    @Transactional
    public MyInfoResponseDto changeInfo(CustomUserDetails userDetails, MyInfoChangeRequestDto myInfoChangeRequestDto){
        User user = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() ->new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        user.update(myInfoChangeRequestDto.getName(),myInfoChangeRequestDto.getProfilePicUrl());

        return  MyInfoResponseDto.builder()
                .name(myInfoChangeRequestDto.getName())
                .email(user.getEmail()) //안바뀜
//                .followingNum(user.getFollows().size()) //안바뀜
                .profilePicUrl(myInfoChangeRequestDto.getProfilePicUrl())
                .build();
    }
}
