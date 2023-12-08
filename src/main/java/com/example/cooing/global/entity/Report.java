package com.example.cooing.global.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "baby_id")
    private Long babyId;

    @Column(name = "month")
    private Integer month;

    @Column(name = "week")
    private Integer week;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "json", name = "secret_note")
    private ArrayList<Boolean> secretNote;

    @Type(JsonType.class)
    @Column(columnDefinition = "json", name = "frequent_words")
    private Map<String, Integer> frequentWords = new HashMap<>();

}
