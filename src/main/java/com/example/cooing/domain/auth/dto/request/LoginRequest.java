package com.example.cooing.domain.auth.dto.request;

import lombok.Data;

@Data
//카카오 서버에 로그인 요청
public class LoginRequest {
    private String providerId;
    private String nickname;
    private String email;
}
