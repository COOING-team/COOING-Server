package com.example.cooing.domain.auth.dto.request;

import com.example.cooing.global.enums.Sex;
import lombok.Data;

import java.time.LocalDate;

@Data
//카카오 서버에 로그인 요청
public class BabyRequest {
    private String name;
    private Sex sex;
    private LocalDate birth;
}