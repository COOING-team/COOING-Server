package com.example.cooing.domain.auth.dto.request;

import com.example.cooing.global.enums.OAuthProvider;
import lombok.Data;

@Data
public class LoginRequest {

    private OAuthProvider oAuthProvider;
    private String name;
    private String email;
}
