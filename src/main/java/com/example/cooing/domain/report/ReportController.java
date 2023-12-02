package com.example.cooing.domain.report;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.global.base.BaseResponseDto;
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

}
