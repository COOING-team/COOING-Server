package com.example.cooing.global.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @Column(name = "baby_id") // baby 테이블과 매핑
    private Long babyId;

    @Column(name = "question_id") // 질문 테이블과 매핑
    private Long questionId;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "comment")
    private String comment;

    @Column(name = "word_count")
    private Integer wordCount;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", name = "morp_result")
    private List<Map> morp = new ArrayList<>();

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", name = "word_result")
    private List<Map> word = new ArrayList<>();

    @Column(name = "create_at")
    private LocalDateTime createAt;

}
