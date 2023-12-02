package com.example.cooing.global.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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

    @Column(name = "cooing_index") // 질문 테이블과 매핑
    private Long cooingIndex;

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
