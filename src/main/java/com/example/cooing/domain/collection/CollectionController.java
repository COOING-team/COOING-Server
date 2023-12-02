package com.example.cooing.domain.collection;

import static com.example.cooing.global.RequestURI.COLLECTION_URI;
import static com.example.cooing.global.RequestURI.QUESTION_URI;

import com.example.cooing.domain.collection.dto.CreateQuestionRequest;
import com.example.cooing.domain.collection.dto.MonthlyAnswerDto;
import com.example.cooing.domain.collection.dto.QuestionResponseDto;
import com.example.cooing.global.base.BaseResponseDto;
import com.example.cooing.global.entity.Answer;
import com.example.cooing.global.entity.Question;
import com.example.cooing.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(COLLECTION_URI)
@RestController
@RequiredArgsConstructor
public class CollectionController {

  private final CollectionService collectionService;

  @GetMapping(value = "/{answerId}")
  @Operation(summary = "[모아보기] 특정 답변을 반환", description = "앞선 api에서 answerId을 넘겨받아야합니다")
  public BaseResponseDto<Answer> getAnswer(
      @PathVariable("answerId") Long answerId) {
    try {
      return BaseResponseDto.success("질문 조회 성공", collectionService.getAnswer(answerId));
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }

  @GetMapping(value = "/{year}/{month}")
  @Operation(summary = "[모아보기] 그해 그월의 답변목록 반환", description = "년과 월을 넘겨주세요")
  public BaseResponseDto<List<MonthlyAnswerDto>> getMonthlyCollection(
      @PathVariable("year") int year,
      @PathVariable("month") int month) {
    try {
      List<MonthlyAnswerDto> monthlyAnswerDto = collectionService.getAllCollectionByMonth(year,
          month);
      return BaseResponseDto.success(month + "월의 답변 목록 조회 성공", monthlyAnswerDto);
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }

//  @GetMapping(value = "/detail")
//  @Operation(summary = "해당 기록 반환", description = "입력한 날의 기록을 상세하게 보여줍니다.")
//  public BaseResponseDto<List<MonthlyAnswerDto>> getDetailAnswer(
//      @RequestParam Integer year,
//      @RequestParam Integer month,
//      @RequestParam Integer day
//  ) {
//    try {
//      List<MonthlyAnswerDto> monthlyAnswerDto = collectionService.getAllCollectionByMonth(year,
//          month);
//      return BaseResponseDto.success("질문 조회 성공", monthlyAnswerDto);
//    } catch (CustomException e) {
//      // 실패 시
//      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
//    }
//  }

//  @PostMapping(value = "/create")
//  @Operation(summary = "해당 질문 추가", description = "질문을 만들어요")
//  public BaseResponseDto<QuestionResponseDto> saveQuestion(
//      CreateQuestionRequest createQuestionRequest) {
//    try {
//      QuestionResponseDto questionResponseDto = questionService.createQuestion(
//          createQuestionRequest);
//      return BaseResponseDto.success("질문 저장 성공", questionResponseDto);
//    } catch (CustomException e) {
//      // 실패 시
//      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
//    }
//  }
//
//  @DeleteMapping(value = "/{cooingIndex}")
//  @Operation(summary = "해당 질문 삭제", description = "질문을 삭제해요")
//  public BaseResponseDto<Void> deleteQuestion(@PathVariable("cooingIndex") Long cooingIndex) {
//    try {
//      questionService.deleteQuestion(cooingIndex);
//      return BaseResponseDto.success("질문 삭제 성공", null);
//    } catch (CustomException e) {
//      // 실패 시
//      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
//    }
//  }
}
