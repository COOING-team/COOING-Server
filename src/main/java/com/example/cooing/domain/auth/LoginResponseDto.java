package com.example.cooing.domain.auth;

import com.example.cooing.global.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;

//    private Role role;
//    private Long userId;
//    private String userName;
}
