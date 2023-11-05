package com.example.cooing.domain.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AnswerService {

    public String saveFileToStorage(MultipartFile multipartFile) {
        return "";
    }
}
