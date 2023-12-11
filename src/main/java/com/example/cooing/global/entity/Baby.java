package com.example.cooing.global.entity;

import com.example.cooing.global.enums.Sex;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Baby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "baby_id")
    private Long id;

    @Column(name = "user_id") // user 테이블과 매핑
    private Long userId;

    @Column(name = "baby_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @JoinColumn(name = "baby_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Report> reportList;

    @JoinColumn(name = "baby_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Answer> answerLists;

}
