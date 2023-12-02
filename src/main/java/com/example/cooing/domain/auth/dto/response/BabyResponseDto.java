package com.example.cooing.domain.auth.dto.response;

import com.example.cooing.global.enums.Role;
import com.example.cooing.global.enums.Sex;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Builder
@Getter
public class BabyResponseDto {
    private String name;
    private Sex sex;
    private LocalDate birth;
}