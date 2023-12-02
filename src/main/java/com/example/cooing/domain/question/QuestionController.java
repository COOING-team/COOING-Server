package com.example.cooing.domain.question;

import static com.example.cooing.global.RequestURI.ANSWER_URI;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(ANSWER_URI)
@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService homeService;
//
//
//    @GetMapping(value = "/info")
//    @Operation(summary = "홈 정보 조회", description = "태어난 지 & 말을 기록한지")
//    public BaseResponseDto<HomeResponseDto> home(@AuthenticationPrincipal CustomUserDetails userDetail) {
//        return BaseResponseDto.success("아이 정보 등록 완료", homeService.getHomeInfo(userDetail));
//    }

}
