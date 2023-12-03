package com.example.cooing.domain.report;

import static com.example.cooing.global.RequestURI.REPORT_URI;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.report.dto.InfoResponseDto;
import com.example.cooing.domain.report.dto.SecretNoteResponse;
import com.example.cooing.domain.report.dto.TotalResponseDto;
import com.example.cooing.global.base.BaseResponseDto;
import com.example.cooing.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(REPORT_URI)
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @GetMapping(value = "/secret-note")
  @Operation(summary = "해당 주차의 레포트 데이터를 받아옵니다.", description = "빈출 단어도 레포트를 생성해야만 갱신됩니다.")
  public BaseResponseDto<SecretNoteResponse> SecretNote(
      @AuthenticationPrincipal CustomUserDetails userDetail,
      @RequestParam("month") Integer month,
      @RequestParam("week") Integer week) {
    return BaseResponseDto.success("ok", reportService.getSecretNote(userDetail, month, week));
  }

  @GetMapping(value = "/info")
  @Operation(summary = "[레포트] 상단정보", description = "n월 n째주 쿠잉이의 주간 레포트 / 000, 태어난지 N개월쨰")
  public BaseResponseDto<InfoResponseDto> getInfo(
      @AuthenticationPrincipal CustomUserDetails userDetail) {
    try {
      return BaseResponseDto.success("info 정보 조회 성공", reportService.getInfo(userDetail));
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }

  @GetMapping(value = "/total")
  @Operation(summary = "[레포트] 요약", description = "쿠잉이가 사용한 단어 수 / 쿠잉이의 애착단어")
  public BaseResponseDto<TotalResponseDto> getTotal(
      @AuthenticationPrincipal CustomUserDetails userDetail) {
    try {
      return BaseResponseDto.success("total 정보 조회 성공", reportService.getTotalInfo(userDetail));
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }
}
