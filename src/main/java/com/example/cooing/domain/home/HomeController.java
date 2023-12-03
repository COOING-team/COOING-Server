package com.example.cooing.domain.home;

import static com.example.cooing.global.RequestURI.HOME_URI;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.home.dto.HomeResponseDto;
import com.example.cooing.global.base.BaseResponseDto;
import com.example.cooing.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(HOME_URI)
@RestController
@RequiredArgsConstructor
public class HomeController {

  private final HomeService homeService;

  @GetMapping(value = "/info")
  @Operation(summary = "[홈] 홈 정보 조회", description = "태어난 지 & 말을 기록한지 & 오늘 기록을 했는지.(이미 기록했다면 True)")
  public BaseResponseDto<HomeResponseDto> home(
      @AuthenticationPrincipal CustomUserDetails userDetail) {
    try {
      return BaseResponseDto.success("홈 정보 조회 성공", homeService.getHomeInfo(userDetail));
    } catch (
        CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }
}
