package com.example.cooing.global.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "user_id") // user 테이블과 매핑
    private Long userId;

    @Column(name = "question_id") // 질문 테이블과 매핑
    private Long questionId;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "create_at")
    private LocalDateTime createAt;
}
