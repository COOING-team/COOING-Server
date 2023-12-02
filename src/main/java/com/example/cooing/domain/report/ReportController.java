package com.example.cooing.domain.report;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.report.dto.TotalInfoResponseDto;
import com.example.cooing.global.base.BaseResponseDto;
import com.example.cooing.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static com.example.cooing.global.RequestURI.REPORT_URI;

@RestController
@RequestMapping(REPORT_URI)
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping(value = "/secret-note")
    @Operation(summary = "해당 주차의 레포트 데이터를 받아옵니다.", description = "빈출 단어도 레포트를 생성해야만 갱신됩니다.")
    public BaseResponseDto<ArrayList<Boolean>> SecretNote(@AuthenticationPrincipal CustomUserDetails userDetail,
                                                          @RequestParam Integer month,
                                                          @RequestParam Integer week) {
        return BaseResponseDto.success("ok", reportService.getSecretNote(userDetail, month, week));
    }

  @GetMapping(value = "/info")
  @Operation(summary = "[total] 레포트 요약", description = "")
  public BaseResponseDto<TotalInfoResponseDto> getAllQuestion(@AuthenticationPrincipal CustomUserDetails userDetail) {
    try {
      return BaseResponseDto.success("레포트 조회 성공", reportService.getTotalInfo(userDetail));
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }
}
