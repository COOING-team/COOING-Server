package com.example.cooing.domain.auth;


import com.example.cooing.domain.auth.dto.request.BabyRequest;
import com.example.cooing.domain.auth.dto.request.LoginRequest;
import com.example.cooing.domain.auth.dto.response.BabyResponseDto;
import com.example.cooing.domain.auth.dto.response.InfoResponseDto;
import com.example.cooing.domain.auth.dto.response.LoginResponseDto;
import com.example.cooing.domain.auth.jwt.JwtService;
import com.example.cooing.global.entity.Baby;
import com.example.cooing.global.entity.User;
import com.example.cooing.global.enums.OAuthProvider;
import com.example.cooing.global.enums.Role;
import com.example.cooing.global.exception.CustomException;
import com.example.cooing.global.repository.BabyRepository;
import com.example.cooing.global.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.cooing.global.util.CalculateWithBirthUtil.getAge;
import static com.example.cooing.global.util.CalculateWithBirthUtil.getMonthsSinceBirth;
import static com.example.cooing.global.exception.CustomErrorCode.NO_BABY;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

  private final JwtService jwtService;

  private final UserRepository userRepository;
  private final BabyRepository babyRepository;


  @Transactional
  public LoginResponseDto login(LoginRequest loginRequest) {

    User user = userRepository.findByEmail(loginRequest.getEmail()).orElseGet(
        () -> saveUser(loginRequest)
    );
    String serviceAccessToken = jwtService.createAccessToken(loginRequest.getEmail());
    String serviceRefreshToken = jwtService.createRefreshToken(loginRequest.getEmail());

    return LoginResponseDto.builder()
        .accessToken(serviceAccessToken)
        .refreshToken(serviceRefreshToken)
        .build();
  }

  @Transactional
  public User saveUser(LoginRequest loginRequest) {

    User member = User.builder()
        .oAuthProvider(OAuthProvider.KAKAO)
        .providerId(loginRequest.getProviderId())
        .email(loginRequest.getEmail())
        .role(Role.USER)
        .name(loginRequest.getNickname())
        .build();
    userRepository.saveAndFlush(member);
    return member;

  }

  @Transactional
  public InfoResponseDto getMyInfo(CustomUserDetails userDetails) {
    User user = userRepository.findByEmail(userDetails.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

    Baby baby = user.getBabyList().get(0); //Todo 아이 한명 이상이면 이 부분을 수정하세요
//    Baby baby = babyRepository.findByUserId(user.getId())
//        .orElseThrow(() -> new CustomException(NO_BABY));

    return InfoResponseDto.builder()
        .name(baby.getName())
        .age(getAge(baby.getBirth()))
        .month(getMonthsSinceBirth(baby.getBirth()))
        .birth(baby.getBirth())
        .sex(baby.getSex())
        .build();
  }


  @Transactional
  public BabyResponseDto createBaby(CustomUserDetails userDetails, BabyRequest babyRequest) {
    User user = userRepository.findByEmail(userDetails.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

    Baby baby = saveBaby(babyRequest);
    user.addBaby(baby);
    return BabyResponseDto.builder()
        .name(babyRequest.getName())
        .sex(babyRequest.getSex())
        .birth(babyRequest.getBirth())
        .build();
  }


  @Transactional
  public Baby saveBaby(BabyRequest babyRequest) {

    Baby baby = Baby.builder()
        .sex(babyRequest.getSex())
        .birth(babyRequest.getBirth())
        .createAt(LocalDateTime.now())
        .name(babyRequest.getName())
        .build();
    babyRepository.saveAndFlush(baby);
    return baby;
  }
}
