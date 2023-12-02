package com.example.cooing.domain.question;

import static com.example.cooing.global.RequestURI.ANSWER_URI;
import static com.example.cooing.global.RequestURI.QUESTION_URI;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.question.dto.QuestionResponseDto;
import com.example.cooing.global.base.BaseResponseDto;
import com.example.cooing.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(QUESTION_URI)
@RestController
@RequiredArgsConstructor
public class QuestionController {

  private final QuestionService questionService;


  @GetMapping(value = "/{questionId}")
  @Operation(summary = "해당 질문 반환", description = "홈에 있는 cooingDay에 +1 한 값을 입력하면 그 다음 질문 번호가 되겠죠?")
  public BaseResponseDto<QuestionResponseDto> home(@PathVariable("questionId") Long questionId) {
    try {
      QuestionResponseDto questionResponseDto = questionService.getQuestion(questionId);
      return BaseResponseDto.success("질문 조회 성공", questionResponseDto);
    } catch (CustomException e) {
      // 실패 시
      return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
    }
  }

}
