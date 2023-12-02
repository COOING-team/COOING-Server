package com.example.cooing.domain.report;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.global.entity.*;
import com.example.cooing.global.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.cooing.global.util.CalculateWeekAndDayUtil.calculateWeekToDay;


@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public ArrayList<Boolean> getSecretNote(CustomUserDetails userDetail, Integer month, Integer week) {
        User user = userRepository.findByEmail(userDetail.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        Baby baby = user.getBabyList().get(0);

        Report report = reportRepository.findByBabyIdAndMonthAndWeek(baby.getId(), month, week)
                .orElseGet(() -> makeReport(baby, month, week));

        return report.getSecretNote();
    }

    private Report makeReport(Baby baby, Integer month, Integer week) {

        ArrayList<LocalDate> daysOfWeek = calculateWeekToDay(month, week);

        List<Answer> answers = answerRepository
                .findAllByCreateAtBetween(daysOfWeek.get(0).atTime(LocalTime.MIN), daysOfWeek.get(6).atTime(LocalTime.MIN));

        ArrayList<Boolean> secretNote = makeSecretNote(answers);
        Map<String, Integer> frequentWords = makeFrequentWords(answers);

        Report report = Report.builder()
                .babyId(baby.getId())
                .month(month)
                .week(week)
                .secretNote(secretNote)
                .frequentWords(frequentWords)
                .build();

        reportRepository.save(report);

        return report;
    }

    private ArrayList<Boolean> makeSecretNote(List<Answer> answers) {
        ArrayList<Boolean> secretNote = new ArrayList<>();

        return secretNote;
    }

    private Map<String, Integer> makeFrequentWords(List<Answer> answers) {

        Map<String, Integer> totalWords = new HashMap<>();

        for (Answer answer: answers) {
            List<Map> wordSet = answer.getWord();
            for (Map map: wordSet) {
                if (!totalWords.containsKey(map.get("word"))) {
                    totalWords.put(map.get("word").toString(), 1);
                }
                else {
                    totalWords.put(map.get("word").toString(), totalWords.get(map.get("word")) + 1);
                }
            }
        }

        Map<String, Integer> frequentWords = getTop5KeysAndValues(totalWords);

        return frequentWords;
    }

    private Map<String, Integer> getTop5KeysAndValues(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
