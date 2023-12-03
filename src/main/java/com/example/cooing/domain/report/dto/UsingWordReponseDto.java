package com.example.cooing.domain.report.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class UsingWordReponseDto {
  private final List<WordCountPerDay> wordNum;
  private final Integer averageWordNum;
}