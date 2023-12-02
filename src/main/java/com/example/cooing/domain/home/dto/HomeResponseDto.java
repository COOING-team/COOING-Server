package com.example.cooing.domain.home.dto;

import com.example.cooing.global.enums.Sex;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomeResponseDto {

  private final String name;

  private final int month;
  private final int cooingDay;
}
