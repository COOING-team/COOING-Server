package com.example.cooing.domain.report;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.report.dto.InfoResponseDto;
import com.example.cooing.domain.report.dto.TotalResponseDto;
import com.example.cooing.global.entity.*;
import com.example.cooing.global.repository.*;
import com.example.cooing.global.util.WeekOfMonthDto;
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
import static com.example.cooing.global.util.CalculateWeekAndDayUtil.getYearMonthWeekInfo;
import static com.example.cooing.global.util.CalculateWithBirthUtil.getMonthsSinceBirth;


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
        //Todo 여기 유저나 베이비 검사 안해도 되는 find 문인지 의문!


//        ArrayList<Boolean> secretNote = makeSecretNote(answers);
        //임시 더미 데이터
        ArrayList<Boolean> secretNote = new ArrayList<>();
        for(int i = 0; i < 17; i++) {
            secretNote.add(true);
        }

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

        ArrayList<Boolean> result1 = evaluateSentenceStructure(answers);
        ArrayList<Boolean> result2 = evaluateMeaning(answers);
        ArrayList<Boolean> result3 = evaluateMorps(answers);

        secretNote.addAll(result1);
        secretNote.addAll(result2);
        secretNote.addAll(result3);

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

    private ArrayList<Boolean> evaluateSentenceStructure(List<Answer> answers) {
        ArrayList<Boolean> result = new ArrayList<>();

//        // 일주일동안 구사한 문장들의 평균 단어 수로 판단
//
//        String[] sentences = text.split("\\.\\s*");
//
//        // 각 문장의 단어 수를 계산하고 출력
//        for (String sentence : sentences) {
//            int wordCount = countWords(sentence);
//            System.out.println("Words in sentence: \"" + sentence + "\": " + wordCount);
//        }

        return result;
    }

    private ArrayList<Boolean> evaluateMeaning(List<Answer> answers) {
        ArrayList<Boolean> result = new ArrayList<>();

        return result;
    }

    private ArrayList<Boolean> evaluateMorps(List<Answer> answers) {
        ArrayList<Boolean> result = new ArrayList<>();

        return result;
    }

    public InfoResponseDto getInfo(CustomUserDetails userDetail) {
        User user = userRepository.findByEmail(userDetail.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        Baby baby = user.getBabyList().get(0);

        WeekOfMonthDto yearMonthWeekInfo = getYearMonthWeekInfo(LocalDate.now());

        return InfoResponseDto.builder()
            .month(yearMonthWeekInfo.getMonth())
            .week(yearMonthWeekInfo.getWeekOfMonth())
            .name(baby.getName())
            .birthMonth(getMonthsSinceBirth(baby.getBirth()))
            .build();
    }


    public TotalResponseDto getTotalInfo(CustomUserDetails userDetail) {
        User user = userRepository.findByEmail(userDetail.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        Baby baby = user.getBabyList().get(0);

        List<Report> allReports = reportRepository.findAllByBabyId(baby.getId());

        Map<String, Integer> totalWords = new HashMap<>();

        // 모든 주차에 대한 빈도수를 누적
        for (Report report : allReports) {
            Map<String, Integer> frequentWords = report.getFrequentWords();
            frequentWords.forEach((word, count) -> totalWords.merge(word, count, Integer::sum));
        }

        // 누적된 빈도수 중 가장 많이 사용된 단어 선택
        Map.Entry<String, Integer> mostUsedWordEntry = getMostUsedWord(totalWords);

        return TotalResponseDto.builder()
            .totalWordNum(totalWords.size()) //Todo 여기 로직 수정 필요
            .mostUseWord(mostUsedWordEntry.getKey())
            .build();
    }

    private Map.Entry<String, Integer> getMostUsedWord(Map<String, Integer> wordCountMap) {
        return wordCountMap.entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .orElse(null);
    }
}
