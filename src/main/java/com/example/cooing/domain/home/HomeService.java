package com.example.cooing.domain.home;

import static com.example.cooing.global.util.CalculateWithBirthUtil.getMonthsSinceBirth;
import static com.example.cooing.global.exception.CustomErrorCode.NO_BABY;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.home.dto.HomeResponseDto;
import com.example.cooing.global.entity.Baby;
import com.example.cooing.global.entity.User;
import com.example.cooing.global.exception.CustomException;
import com.example.cooing.global.repository.AnswerRepository;
import com.example.cooing.global.repository.BabyRepository;
import com.example.cooing.global.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserRepository userRepository;

    private final BabyRepository babyRepository;
    private final AnswerRepository answerRepository;


    public HomeResponseDto getHomeInfo(CustomUserDetails userDetail) {

        User user = userRepository.findByEmail(userDetail.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        Baby baby = babyRepository.findByUserId(user.getId())
            .orElseThrow(() -> new CustomException(NO_BABY));

        return HomeResponseDto.builder()
            .name(baby.getName())
            .cooingDay(answerRepository.countByBabyId(baby.getId()))
            .month(getMonthsSinceBirth(baby.getBirth()))
            .build();
    }

}
