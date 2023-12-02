package com.example.cooing.domain.report;

import static com.example.cooing.global.RequestURI.QUESTION_URI;
import static com.example.cooing.global.RequestURI.REPORT_URI;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.home.dto.HomeResponseDto;
import com.example.cooing.domain.report.dto.QuestionResponseDto;
import com.example.cooing.domain.report.dto.CreateQuestionRequest;
import com.example.cooing.domain.report.dto.TotalInfoResponseDto;
import com.example.cooing.global.base.BaseResponseDto;
import com.example.cooing.global.entity.Question;
import com.example.cooing.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(REPORT_URI)
@RestController
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

//  @GetMapping(value = "/info")
//  @Operation(summary = "[total] 레포트 요약", description = "")
//  public BaseResponseDto<TotalInfoResponseDto> getAllQuestion(@AuthenticationPrincipal CustomUserDetails userDetail) {
//    try {
//      return BaseResponseDto.success("레포트 조회 성공", reportService.getTotalInfo(userDetail);
//    } catch (CustomException e) {
//      // 실패 시
//      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
//    }
//  }

}
