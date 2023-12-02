package com.example.cooing.domain.collection;

import static com.example.cooing.global.RequestURI.COLLECTION_URI;

import com.example.cooing.domain.collection.dto.AnswerResponseDto;
import com.example.cooing.domain.collection.dto.MonthlyAnswerDto;
import com.example.cooing.global.base.BaseResponseDto;
import com.example.cooing.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(COLLECTION_URI)
@RestController
@RequiredArgsConstructor
public class CollectionController {

  private final CollectionService collectionService;

  @GetMapping(value = "/{year}/{month}")
  @Operation(summary = "[모아보기] 그해 그월의 답변목록 반환", description = "년과 월을 넘겨주세요")
  public BaseResponseDto<List<MonthlyAnswerDto>> getMonthlyCollection(
      @PathVariable("year") int year,
      @PathVariable("month") int month) {
    try {
      List<MonthlyAnswerDto> monthlyAnswerDto = collectionService.getAllCollectionByMonth(year,
          month);
      return BaseResponseDto.success(year+"년 "+month + "월의 답변 목록 조회 성공", monthlyAnswerDto);
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }

  @GetMapping(value = "/{answerId}")
  @Operation(summary = "[모아보기] 특정 답변을 반환", description = "앞선 api에서 answerId을 넘겨받아야합니다")
  public BaseResponseDto<AnswerResponseDto> getAnswer(
      @PathVariable("answerId") Long answerId) {
    try {
      return BaseResponseDto.success( answerId+"번 기록 조회 성공", collectionService.getAnswer(answerId));
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }

}
