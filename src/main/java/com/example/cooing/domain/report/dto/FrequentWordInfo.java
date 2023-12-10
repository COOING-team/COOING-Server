package com.example.cooing.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FrequentWordInfo {
    private String word;
    private Integer count;
}
