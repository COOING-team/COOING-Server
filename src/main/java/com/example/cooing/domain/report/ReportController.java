package com.example.cooing.domain.report;

import static com.example.cooing.global.RequestURI.REPORT_URI;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.report.dto.*;
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
    @Operation(summary = "요청한 주차의 시크릿 노트를 받아옵니다. 빈출 단어도 시크릿 노트를 생성해야만 갱신됩니다.", description = "레포트가 없다면 생성하여 받아옵니다.")
    public BaseResponseDto<SecretNoteResponse> SecretNote(
            @AuthenticationPrincipal CustomUserDetails userDetail, @RequestParam("month") Integer month,
            @RequestParam("week") Integer week) {
        return BaseResponseDto.success("ok", reportService.getSecretNote(userDetail, month, week));
    }

    @GetMapping(value = "/info")
    @Operation(summary = "[레포트] 상단 정보", description = "n월 n째주 쿠잉이의 주간 레포트 / 000, 태어난지 N개월쨰")
    public BaseResponseDto<InfoResponseDto> getInfo(@AuthenticationPrincipal CustomUserDetails userDetail) {
        try {
            return BaseResponseDto.success("info 정보 조회 성공", reportService.getInfo(userDetail));
        } catch (CustomException e) {
            // 실패 시
            return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/secret-note-list")
    @Operation(summary = "해당 월의 시크릿 노트 목록", description = "")
    public BaseResponseDto<SecretNoteListResponse> SecretNoteList(@AuthenticationPrincipal CustomUserDetails userDetail,
                                                                  @RequestParam Integer month) {
        return BaseResponseDto.success("ok", reportService.getSecretNoteList(userDetail, month));
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

    @GetMapping(value = "/using-word")
    @Operation(summary = "[레포트] 사용 단어 수", description = "년/월/주차를 입력하면 사용단어에 대한 리스트를 반환합니다. 이걸 차트로 바인딩하시면 됩니다.")
    public BaseResponseDto<UsingWordReponseDto> getUsingWordChart(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month,
            @RequestParam("week") Integer week) {
        try {
            return BaseResponseDto.success("", reportService.getChart(userDetail, year, month, week));
        } catch (CustomException e) {
            // 실패 시
            return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/frequent")
    @Operation(summary = "빈출 단어", description = "가장 최근 레포트를 만든 주의 빈출 단어를 받아옵니다.")
    public BaseResponseDto<FrequentWordResponse> frequentWords(@AuthenticationPrincipal CustomUserDetails userDetail) {
        return BaseResponseDto.success("ok", reportService.getFrequentWords(userDetail));
    }
}
