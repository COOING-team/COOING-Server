package com.example.cooing.domain.answer;

import com.example.cooing.global.entity.Answer;
import com.example.cooing.global.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.example.cooing.domain.answer.dto.CreateAnswerRequest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    private final AnswerRepository answerRepository;


    public String saveFileToStorage(MultipartFile multipartFile) {
        // 우선은 서버 로컬에 저장하고, 계정 문제 해결되면 외부 스토리지

        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        String fileName = "cooing-" + UUID.randomUUID() + "." + fileExtension;

        String filePath = uploadDirectory + File.separator + fileName;

        try {
            multipartFile.transferTo(new File(filePath));
        } catch (IOException e) {
            return e.getStackTrace()[0].toString();
        }

        return filePath;
    }

    public String createAnswer(CreateAnswerRequest createAnswerRequest) {
        Long userId = 1L; // 임시 값
        Long questionId = 1L; // 임시 값

        try {
            Answer answer = Answer.builder()
                    .userId(userId)
                    .questionId(questionId)
                    .fileUrl(createAnswerRequest.getFileUrl())
                    .answerText(createAnswerRequest.getAnswerText())
                    .createAt(LocalDateTime.now())
                    .build();

            answerRepository.save(answer);
        } catch (Exception e) {
            return e.getStackTrace()[0].toString();
        }

        return "등록 성공";
    }
}