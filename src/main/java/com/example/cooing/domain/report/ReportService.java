package com.example.cooing.domain.report;

import static com.example.cooing.global.exception.CustomErrorCode.EXIST_QUESTION;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.question.dto.QuestionResponseDto;
import com.example.cooing.domain.report.dto.CreateQuestionRequest;
import com.example.cooing.domain.report.dto.TotalInfoResponseDto;
import com.example.cooing.global.entity.Question;
import com.example.cooing.global.exception.CustomErrorCode;
import com.example.cooing.global.exception.CustomException;
import com.example.cooing.global.repository.QuestionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

//  public TotalInfoResponseDto getTotalInfo(CustomUserDetails userDetail) {
//
//    Question question = questionRepository.findByCooingIndex(cooingIndex)
//        .orElseThrow(() -> new CustomException(CustomErrorCode.NO_QUESTION));
//
//    return QuestionResponseDto.builder()
//        .questionId(question.getId())
//        .cooingIndex(question.getCooingIndex())
//        .content(question.getContent())
//        .build();
//  }
}
