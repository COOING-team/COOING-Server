package com.example.cooing.domain.answer;

import com.example.cooing.domain.answer.dto.CreateAnswerRequest;
import com.example.cooing.domain.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.cooing.global.RequestURI.ANSWER_URI;

@RequestMapping(ANSWER_URI)
@RestController
@RequiredArgsConstructor
public class AnswerController {

  private final AnswerService answerService;

  @PostMapping(value = "/save-audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "음성 파일 저장", description = "음성파일을 서버에 저장하고 파일이 저장 된 경로를 리턴합니다.")
  public ResponseEntity<String> saveAudioFile(
      @RequestParam("audioFile") MultipartFile multipartFile) {
    return ResponseEntity.ok(answerService.saveFileToStorage(multipartFile));
  }

  @PostMapping("/create")
  @Operation(summary = "최종 질문 텍스트와 음성파일 URL 등록", description = "음성 파일을 서버에 저장한 뒤 얻은 URL을 넣어주세요.")
  public ResponseEntity<String> createAnswerData(
      @AuthenticationPrincipal CustomUserDetails userDetail,
      @RequestBody CreateAnswerRequest createAnswerRequest) {
    return ResponseEntity.ok(answerService.createAnswer(userDetail, createAnswerRequest));
  }

}
