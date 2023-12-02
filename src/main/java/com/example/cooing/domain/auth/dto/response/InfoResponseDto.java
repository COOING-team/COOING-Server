package com.example.cooing.domain.auth.dto.response;

import com.example.cooing.global.enums.Sex;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class InfoResponseDto {

    private final int month;

    private final String name;

    private final Sex sex;
    private final int age;
    private final LocalDate birth;
}
