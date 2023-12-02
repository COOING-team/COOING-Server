package com.example.cooing.domain.home;

import static com.example.cooing.global.RequestURI.ANSWER_URI;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.home.dto.HomeResponseDto;
import com.example.cooing.global.base.BaseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(ANSWER_URI)
@RestController
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping(value = "/info")
    @Operation(summary = "홈 정보 조회", description = "태어난 지 & 말을 기록한지")
    public BaseResponseDto<HomeResponseDto> home(@AuthenticationPrincipal CustomUserDetails userDetail) {
        return BaseResponseDto.success("아이 정보 등록 완료", homeService.getHomeInfo(userDetail));
    }

}
