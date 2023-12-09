package com.example.cooing.domain.report.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class SecretNoteResponse {
    private List<Boolean> grade1;
    private List<Boolean> grade2;
    private List<Boolean> grade3;

    public SecretNoteResponse(Map<String, Boolean> secretNote) {
        this.grade1 = new ArrayList<>();
        this.grade2 = new ArrayList<>();
        this.grade3 = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String idx = String.valueOf(i);
            grade1.add(secretNote.get(idx));
        }

        for (int i = 6; i <= 12; i++) {
            String idx = String.valueOf(i);
            grade2.add(secretNote.get(idx));
        }

        for (int i = 13; i <= 17; i++) {
            String idx = String.valueOf(i);
            grade3.add(secretNote.get(idx));
        }

    }
}
