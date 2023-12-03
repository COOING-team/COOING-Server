package com.example.cooing.domain.report.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SecretNoteResponse {
    private List<Boolean> grade1;
    private List<Boolean> grade2;
    private List<Boolean> grade3;

    public SecretNoteResponse(ArrayList<Boolean> secretNote) {
        this.grade1 = secretNote.subList(0, 5);
        this.grade2 = secretNote.subList(5, 12);
        this.grade3 = secretNote.subList(12, 17);
    }
}
