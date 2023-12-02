package com.example.cooing.domain.auth;



import com.example.cooing.domain.auth.jwt.JwtService;
import com.example.cooing.domain.auth.kakao.req.LoginRequest;
import com.example.cooing.global.entity.Baby;
import com.example.cooing.global.entity.User;
import com.example.cooing.global.enums.OAuthProvider;
import com.example.cooing.global.enums.Role;
import com.example.cooing.global.repository.BabyRepository;
import com.example.cooing.global.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final BabyRepository babyRepository;


    @Transactional
    public LoginResponseDto login(LoginRequest loginRequest){

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseGet(
                ()->saveUser(loginRequest)
        );
        String serviceAccessToken= jwtService.createAccessToken(loginRequest.getEmail());
        String serviceRefreshToken= jwtService.createRefreshToken(loginRequest.getEmail());



        return LoginResponseDto.builder()
                .accessToken(serviceAccessToken)
                .refreshToken(serviceRefreshToken)
                .build();
    }

    @Transactional
    public User saveUser(LoginRequest loginRequest){

        User member = User.builder()
                .oAuthProvider(OAuthProvider.KAKAO)
                .providerId(loginRequest.getProviderId())
                .email(loginRequest.getEmail())
                .role(Role.USER)
                .name(loginRequest.getNickname())
                .build();
        userRepository.saveAndFlush(member);
        return  member;

    }

//    @Transactional
//    public MyInfoResponseDto getMyInfo(CustomUserDetails userDetails){
//        User user = userRepository.findByEmail(userDetails.getEmail())
//                .orElseThrow(() ->new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
//
//        Baby baby = babyRepository.findByUserId(user.getId())
//                .orElseThrow(()-> Exception("등록한 아이가 없습니다."))
//
//
//        //Todo 프로필 이미지 넣어야함
//        return  MyInfoResponseDto.builder()
//                .name(userDetails.getUsername())
//                .email(user.getEmail())
//                .profilePicUrl(user.getProfilePicUrl())
//                .build();
//    }

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

    @Transactional
    public BabyResponseDto createBaby(CustomUserDetails userDetails, BabyRequest babyRequest) {
        User user = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() ->new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        Baby baby = saveBaby(babyRequest);
        user.addBaby(baby);
        return BabyResponseDto.builder()
                .name(babyRequest.getName())
                .sex(babyRequest.getSex())
                .birth(babyRequest.getBirth())
                .build();
    }


    @Transactional
    public Baby saveBaby(BabyRequest babyRequest){

        Baby baby = Baby.builder()
                .sex(babyRequest.getSex())
                .birth(babyRequest.getBirth())
                .createAt(LocalDateTime.now())
                .name(babyRequest.getName())
                .build();
        babyRepository.saveAndFlush(baby);
        return  baby;
    }
}
