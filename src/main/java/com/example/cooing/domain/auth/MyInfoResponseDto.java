package com.example.cooing.domain.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyInfoResponseDto {

    private final String name;
    private final String email;
    private final String profilePicUrl;
    private final int followingNum;
}
