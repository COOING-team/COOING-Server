package com.example.cooing.global.repository;

import com.example.cooing.global.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByBabyIdAndMonthAndWeek(Long babyId, Integer month, Integer week);

}
