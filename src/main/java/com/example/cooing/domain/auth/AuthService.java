package com.example.cooing.domain.auth;


import static com.example.cooing.global.exception.CustomErrorCode.EXIST_EMAIL;
import static com.example.cooing.global.exception.CustomErrorCode.NO_BABY;
import static com.example.cooing.global.util.CalculateWithBirthUtil.getAge;
import static com.example.cooing.global.util.CalculateWithBirthUtil.getMonthsSinceBirth;

import com.example.cooing.domain.auth.dto.request.BabyRequest;
import com.example.cooing.domain.auth.dto.request.LoginRequest;
import com.example.cooing.domain.auth.dto.response.BabyResponseDto;
import com.example.cooing.domain.auth.dto.response.InfoResponseDto;
import com.example.cooing.domain.auth.dto.response.LoginResponseDto;
import com.example.cooing.domain.auth.jwt.JwtService;
import com.example.cooing.global.entity.Baby;
import com.example.cooing.global.entity.User;
import com.example.cooing.global.enums.Role;
import com.example.cooing.global.exception.CustomException;
import com.example.cooing.global.repository.BabyRepository;
import com.example.cooing.global.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        if (user.getOAuthProvider() != loginRequest.getOAuthProvider()) {
            throw new CustomException(EXIST_EMAIL);
        }

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
            .oAuthProvider(loginRequest.getOAuthProvider())
            .email(loginRequest.getEmail())
            .role(Role.USER)
            .name(loginRequest.getName())
            .build();
        userRepository.saveAndFlush(member);
        return member;
    }


    @Transactional
    public InfoResponseDto getMyInfo(CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        List<Baby> babyList = user.getBabyList();

        if (babyList.isEmpty()) {
            // Throw a custom exception when there is no baby in the list
            throw new CustomException(NO_BABY);
        } else {
            Baby baby = user.getBabyList().get(0); //Todo 아이 한명 이상이면 이 부분을 수정하세요
            return InfoResponseDto.builder()
                .name(baby.getName())
                .age(getAge(baby.getBirth()))
                .month(getMonthsSinceBirth(baby.getBirth()))
                .birth(baby.getBirth())
                .sex(baby.getSex())
                .build();
        }
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
