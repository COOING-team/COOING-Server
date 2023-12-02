package com.example.cooing.domain.question;

import static com.example.cooing.global.RequestURI.QUESTION_URI;

import com.example.cooing.domain.question.dto.CreateQuestionRequest;
import com.example.cooing.domain.question.dto.QuestionResponseDto;
import com.example.cooing.global.entity.Question;
import com.example.cooing.global.exception.CustomException;
import com.example.cooing.global.base.BaseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(QUESTION_URI)
@RestController
@RequiredArgsConstructor
public class QuestionController {

  private final QuestionService questionService;

  @GetMapping(value = "/all")
  @Operation(summary = "전체 질문 반환", description = "모든 질문을 반환")
  public BaseResponseDto<List<Question>> getAllQuestion() {
    try {
      return BaseResponseDto.success("질문 조회 성공", questionService.getAllQuestion());
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }
  @GetMapping(value = "/{cooingIndex}")
  @Operation(summary = "[홈] 해당 질문 반환", description = "홈에 있는 cooingDay에 +1 한 값을 입력하면 그 다음 질문 번호가 되겠죠?")
  public BaseResponseDto<QuestionResponseDto> getQuestion(@PathVariable("cooingIndex") Long cooingIndex) {
    try {
      QuestionResponseDto questionResponseDto = questionService.getQuestion(cooingIndex);
      return BaseResponseDto.success("질문 조회 성공", questionResponseDto);
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }
  @PostMapping(value = "/create")
  @Operation(summary = "해당 질문 추가", description = "질문을 만들어요")
  public BaseResponseDto<QuestionResponseDto> saveQuestion(
      @RequestParam("createQuestionRequest") CreateQuestionRequest createQuestionRequest) {
    try {
      QuestionResponseDto questionResponseDto = questionService.createQuestion(createQuestionRequest);
      return BaseResponseDto.success("질문 저장 성공", questionResponseDto);
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }
  @DeleteMapping(value = "/{cooingIndex}")
  @Operation(summary = "해당 질문 삭제", description = "질문을 삭제해요")
  public BaseResponseDto<Void> deleteQuestion(@PathVariable("cooingIndex") Long cooingIndex) {
    try {
      questionService.deleteQuestion(cooingIndex);
      return BaseResponseDto.success("질문 삭제 성공", null);
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }
}
