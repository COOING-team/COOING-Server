package com.example.cooing.domain.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

//    private final UserRepository userRepository;
//
//    private final BabyRepository babyRepository;
//    private final AnswerRepository answerRepository;
//
//
//    public HomeResponseDto getHomeInfo(CustomUserDetails userDetail) {
//
//        User user = userRepository.findByEmail(userDetail.getEmail())
//            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
//
//        Baby baby = babyRepository.findByUserId(user.getId())
//            .orElseThrow(() -> new CustomException(NO_BABY));
//
//        return HomeResponseDto.builder()
//            .name(baby.getName())
//            .cooingDay(answerRepository.countByBabyId(baby.getId()))
//            .month(getMonthsSinceBirth(baby.getBirth()))
//            .build();
//    }

}
