package com.example.cooing.domain.report.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WordCountPerDay {
  private LocalDate day;
  private int wordCount;
}