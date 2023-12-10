package com.example.cooing.domain.report;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.report.dto.*;
import com.example.cooing.global.entity.*;
import com.example.cooing.global.enums.NoteStatus;
import com.example.cooing.global.repository.*;
import com.example.cooing.global.util.WeekOfMonthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.cooing.global.enums.NoteStatus.*;
import static com.example.cooing.global.util.CalculateWeekAndDayUtil.calculateWeekToDay;
import static com.example.cooing.global.util.CalculateWeekAndDayUtil.getYearMonthWeekInfo;
import static com.example.cooing.global.util.CalculateWithBirthUtil.getMonthsSinceBirth;
import static com.example.cooing.global.util.CalculateYearAndMonthUtil.getTotalWeekOfMonth;

import com.example.cooing.domain.report.dto.InfoResponseDto;
import com.example.cooing.domain.report.dto.SecretNoteResponse;
import com.example.cooing.domain.report.dto.TotalResponseDto;
import com.example.cooing.domain.report.dto.UsingWordReponseDto;
import com.example.cooing.domain.report.dto.WordCountPerDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public SecretNoteResponse getSecretNote(CustomUserDetails userDetail, Integer month,
                                            Integer week) {
        User user = userRepository.findByEmail(userDetail.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        Baby baby = user.getBabyList().get(0);

        Report report = reportRepository.findByBabyIdAndMonthAndWeek(baby.getId(), month, week)
                .orElseGet(() -> makeReport(baby, month, week));

        SecretNoteResponse secretNoteResponse = new SecretNoteResponse(report.getSecretNote());

        return secretNoteResponse;
    }

    public SecretNoteListResponse getSecretNoteList(CustomUserDetails userDetail, Integer month) {
        User user = userRepository.findByEmail(userDetail.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        Baby baby = user.getBabyList().get(0);

        ArrayList<SecretNoteList> secretNoteLists = confirmSecretNoteStatus(baby.getId(), month);

        SecretNoteListResponse secretNoteListResponse = new SecretNoteListResponse(secretNoteLists);

        return secretNoteListResponse;
    }

    public FrequentWordResponse getFrequentWords(CustomUserDetails userDetail) {
        List<String> orderStrings = Arrays.asList("firstWord", "secondWord", "thirdWord", "fourthWord", "fifthWord");

        User user = userRepository.findByEmail(userDetail.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        Baby baby = user.getBabyList().get(0);

        List <Report> reports = reportRepository.findAllByBabyId(baby.getId());

        if (reports.isEmpty()) {
            HashMap<String, FrequentWordInfo> wordMap = new HashMap<String, FrequentWordInfo>();
            for (int i = 0; i < 5; i++) {
                FrequentWordInfo frequentWordInfo = new FrequentWordInfo("none", -1);
                wordMap.put(orderStrings.get(i), frequentWordInfo);
            }
            return new FrequentWordResponse(wordMap);
        }

        // 효율성 좀 더 고민해보기
        // 레포트에 year 컬럼 추후 추가 검토
        Collections.sort(reports, Comparator.comparing(Report::getMonth, Comparator.reverseOrder())
                .thenComparing(Report::getWeek, Comparator.reverseOrder()));

        Map<String, Integer> frequentWords = reports.get(0).getFrequentWords();

        ArrayList<FrequentWordInfo> frequentWordInfos = new ArrayList<>();
        for (String key: frequentWords.keySet()) {
            FrequentWordInfo frequentWordInfo = new FrequentWordInfo(key, frequentWords.get(key));
            frequentWordInfos.add(frequentWordInfo);
        }

        Collections.sort(frequentWordInfos, new Comparator<FrequentWordInfo>() {
            @Override
            public int compare(FrequentWordInfo o1, FrequentWordInfo o2) {
                // 내림차순 정렬
                return Integer.compare(o2.getCount(), o1.getCount());
            }
        });

        HashMap<String, FrequentWordInfo> wordMap = new HashMap<String, FrequentWordInfo>();
        for (int i = 0; i < 5; i++) {
            wordMap.put(orderStrings.get(i), frequentWordInfos.get(i));
        }

        if (wordMap.size() < 5) {
            for (int i = wordMap.size(); i < 5; i++) {
                FrequentWordInfo frequentWordInfo = new FrequentWordInfo("none", -1);
                wordMap.put(orderStrings.get(i), frequentWordInfo);
            }
        }

        return new FrequentWordResponse(wordMap);
    }

    private ArrayList<SecretNoteList> confirmSecretNoteStatus(Long babyId, Integer month) {
        ArrayList<SecretNoteList> secretNoteLists = new ArrayList<>();
        Integer weekCount = getTotalWeekOfMonth(month);

        for (int i = 1; i <= weekCount; i++) {
            NoteStatus noteStatus = verifyReportStatue(babyId, month, i);

            SecretNoteList secretNoteList = SecretNoteList.builder()
                    .noteStatus(noteStatus)
                    .month(month)
                    .week(i)
                    .build();
            secretNoteLists.add(secretNoteList);
        }

        return secretNoteLists;
    }

    private NoteStatus verifyReportStatue(Long babyId, Integer month, Integer week) {
        Optional<Report> report = reportRepository.findByBabyIdAndMonthAndWeek(babyId, month, week);

        if (report.isPresent()) {
            return READABLE;
        } else {
            ArrayList<LocalDate> dayOfWeek = calculateWeekToDay(month, week);
            List<Answer> answers =
                    answerRepository.findAllByCreateAtBetweenAndBabyId(dayOfWeek.get(0).atTime(LocalTime.MIN),
                            dayOfWeek.get(6).atTime(LocalTime.MAX), babyId);

            if (answers.size() >= 3) {
                return CREATABLE;
            } else {
                return NOT_CREATABLE;
            }
        }
    }

    private Report makeReport(Baby baby, Integer month, Integer week) {

        ArrayList<LocalDate> daysOfWeek = calculateWeekToDay(month, week);

        List<Answer> answers = answerRepository
                .findAllByCreateAtBetweenAndBabyId(daysOfWeek.get(0).atTime(LocalTime.MIN),
                        daysOfWeek.get(6).atTime(LocalTime.MAX),
                        baby.getId());

        Map<String, Boolean> secretNote = makeSecretNote(answers);

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

    private Map<String, Boolean> makeSecretNote(List<Answer> answers) {
        Map<String, Boolean> secretNote = new HashMap<>();

        List<Boolean> result1 = evaluateSentenceStructure(answers);
        List<Boolean> result2 = evaluateMeaning(answers);
        List<Boolean> result3 = evaluateMorps(answers);

        // 1단계 데이터
        secretNote.put("1", result1.get(0));
        secretNote.put("2", result2.get(0));
        secretNote.put("3", result2.get(1));
        secretNote.put("4", result2.get(2));
        secretNote.put("5", result3.get(0));

        // 2단계 데이터
        secretNote.put("6", result1.get(1));
        secretNote.put("7", result2.get(3));
        secretNote.put("8", result2.get(4));
        secretNote.put("9", result2.get(5));
        secretNote.put("10", result3.get(1));
        secretNote.put("11", result3.get(2));
        secretNote.put("12", result3.get(3));

        // 3단계 데이터
        secretNote.put("13", result1.get(2));
        secretNote.put("14", result2.get(6));
        secretNote.put("15", result2.get(7));
        secretNote.put("16", result3.get(4));
        secretNote.put("17", result3.get(5));

        return secretNote;
    }

    private Map<String, Integer> makeFrequentWords(List<Answer> answers) {

        Map<String, Integer> totalWords = new HashMap<>();

        for (Answer answer : answers) {
            List<Map> wordSet = answer.getWord();
            for (Map map : wordSet) {
                if (!totalWords.containsKey(map.get("word"))) {
                    totalWords.put(map.get("word").toString(), 1);
                } else {
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

        int totalSentence = 0;
        int totalWord = 0;

        for (Answer answer : answers) {
            String text = answer.getAnswerText();
            String[] sentences = text.split("[\\.\\?!]\\s*");
            totalSentence += sentences.length;

            for (String sentence : sentences) {
                String[] words = sentence.split("\\s+");
                totalWord += words.length;
            }
        }

        float avg = (float) totalWord / totalSentence;
        ArrayList<Boolean> result = new ArrayList<>();

        if (avg < 1.5) {
            result.add(true);
            result.add(false);
            result.add(false);
        } else if (avg < 2.5) {
            result.add(true);
            result.add(true);
            result.add(false);
        } else {
            result.add(true);
            result.add(true);
            result.add(true);
        }

        return result;
    }

    private List<Boolean> evaluateMeaning(List<Answer> answers) {
        // 7번 관계어 처리는 추후 구현. 현재는 false로만 리턴.

        List<Boolean> result = Arrays.asList(false, false, false, false, false, false, false, false);

        for (Answer answer : answers) {
            List<Map> morps = answer.getMorp();
            for (Map morp : morps) {
                String text = morp.get("morp").toString();
                String type = morp.get("type").toString();

                // 명사를 나타난다.
                if (type.equals("NNP") || type.equals("NNG") || type.equals("NNB")) {
                    result.set(0, true);
                }

                // 대명사가 나타난다.
                else if (type.equals("NP")) {
                    result.set(1, true);
                    // 누구, 어디, 무엇 등 대명사인 의문사가 나타난다.
                    if (text.equals("누구") || text.equals("어디") || text.equals("무엇")) {
                        result.set(5, true);
                    }
                }

                // 동사와 형용사가 나타난다.
                else if (type.equals("VV") || type.equals("VA")) {
                    result.set(2, true);
                    // 복잡한 동사가 나타난다.
                    if (text.length() >= 2) {
                        result.set(3, true);
                    }
                    // 대명사 외의 의문사가 나타난다.
                    if (text.equals("이렇") || text.equals("저렇") || text.equals("어떻")) {
                        result.set(6, true);
                    }
                }

                // 부사가 나타난다.
                else if (type.equals("MAG") || type.equals("MAJ")) {
                    result.set(4, true);
                    // 대명사 외의 의문사가 나타난다.
                    if (text.equals("왜")) {
                        result.set(6, true);
                    }
                }

            }
        }

        return result;
    }

    private List<Boolean> evaluateMorps(List<Answer> answers) {

        List<Boolean> result = Arrays.asList(false, false, false, false, false, false);

        for (Answer answer : answers) {
            List<Map> morps = answer.getMorp();
            for (Map morp : morps) {
                String text = morp.get("morp").toString();
                String type = morp.get("type").toString();

                // 종결어미가 나타난다.
                if (type.equals("EF")) {
                    result.set(0, true);
                    // 복잡한 종결어미가 나타난다.
                    if (text.length() >= 2) {
                        result.set(5, true);
                    }
                }

                // 의존명사가 나타난다.
                else if (type.equals("NNB")) {
                    result.set(1, true);
                }

                // 주격 조사가 나타난다.
                else if (type.equals("JKS")) {
                    result.set(2, true);
                }

                // 보조사가 나타난다.
                else if (type.equals("JX") || type.equals("JC")) {
                    result.set(3, true);
                }

                // 연결어미가 나타난다
                else if (type.equals("EC")) {
                    result.set(4, true);
                }
            }
        }

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

        System.out.println(allReports);

        Map<String, Integer> totalWords = new HashMap<>();

        // 모든 주차에 대한 빈도수를 누적
        for (Report report : allReports) {
            Map<String, Integer> frequentWords = report.getFrequentWords();
            frequentWords.forEach((word, count) -> totalWords.merge(word, count, Integer::sum));
        }

        // 누적된 빈도수 중 가장 많이 사용된 단어 선택
        Map.Entry<String, Integer> mostUsedWordEntry = getMostUsedWord(totalWords);

        System.out.println(totalWords.size());

        if (mostUsedWordEntry != null) {
            System.out.println(mostUsedWordEntry.getKey());

            return TotalResponseDto.builder()
                    .totalWordNum(totalWords.size())
                    .mostUseWord(mostUsedWordEntry.getKey())
                    .build();
        } else {
            // Handle the case where mostUsedWordEntry is null
            // throw new CustomException(NO_YET_REPORT);
            return TotalResponseDto.builder()
                    .totalWordNum(0)
                    .mostUseWord(null)
                    .build();
        }
    }


    private Map.Entry<String, Integer> getMostUsedWord(Map<String, Integer> wordCountMap) {
        return wordCountMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
    }


    public UsingWordReponseDto getChart(CustomUserDetails userDetail, Integer year, Integer month,
                                        Integer week) {
        User user = userRepository.findByEmail(userDetail.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        Baby baby = user.getBabyList().get(0);

        ArrayList<LocalDate> weekOfDays = calculateWeekToDay(year, month, week);

        List<Answer> answers = answerRepository.findAllByCreateAtBetweenAndBabyId(
                weekOfDays.get(0).atTime(LocalTime.MIN),
                weekOfDays.get(6).atTime(LocalTime.MAX),
                baby.getId()
        );
        System.out.println(weekOfDays.get(0) + weekOfDays.get(6).toString());

        List<WordCountPerDay> wordCountList = new ArrayList<>();

        for (LocalDate day : weekOfDays) {
            int totalWordCount = answers.stream()
                    .filter(answer -> answer.getCreateAt().toLocalDate().isEqual(day))
                    .mapToInt(Answer::getWordCount)
                    .sum();

            wordCountList.add(new WordCountPerDay(day, totalWordCount));
        }
        System.out.println(wordCountList.get(0));
        System.out.println(wordCountList.get(1));
        System.out.println(wordCountList.get(2));
        System.out.println(wordCountList.get(3));
        System.out.println(wordCountList.get(4));
        System.out.println(wordCountList.get(5));
        System.out.println(wordCountList.get(6));


        // Now you can use the wordCountList as needed
        return UsingWordReponseDto.builder()
                .averageWordNum(calculateAverageWordNum(wordCountList))
                .wordNum(wordCountList)
                .build();
    }

    private int calculateAverageWordNum(List<WordCountPerDay> wordCountList) {
        // Filter out days with wordCount equal to 0
        List<WordCountPerDay> nonZeroWordCountList = wordCountList.stream()
                .filter(day -> day.getWordCount() > 0)
                .collect(Collectors.toList());

        // Calculate the total word count only for days with non-zero wordCount
        int totalWordCount = nonZeroWordCountList.stream()
                .mapToInt(WordCountPerDay::getWordCount)
                .sum();

        // Calculate the average only if there are non-zero wordCount days
        return nonZeroWordCountList.isEmpty() ? 0 : totalWordCount / nonZeroWordCountList.size();
    }
}
