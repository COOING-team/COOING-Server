package com.example.cooing.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class FrequentWordResponse {
    private Map<String, Integer> frequentWords;
}
