package com.example.cooing.domain.report.dto;

import com.example.cooing.global.enums.NoteStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SecretNoteList {
    private Integer month;
    private Integer week;
    private NoteStatus noteStatus;
}
