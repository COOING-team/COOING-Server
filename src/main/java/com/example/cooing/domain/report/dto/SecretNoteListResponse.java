package com.example.cooing.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class SecretNoteListResponse {
    private ArrayList<SecretNoteList> secretNoteLists;
}
