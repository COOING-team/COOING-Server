package com.example.cooing.domain.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {

    @Value("${upload.directory}")
    private String uploadDirectory;


    public String saveFileToStorage(MultipartFile multipartFile) {
        // 우선은 서버 로컬에 저장하고, 계정 문제 해결되면 외부 스토리지

        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        String fileName = "cooing-" + UUID.randomUUID() + "." + fileExtension;

        String filePath = uploadDirectory + File.separator + fileName;

        try {
            multipartFile.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return "업로드 실패";
        }

        return filePath;
    }
}
