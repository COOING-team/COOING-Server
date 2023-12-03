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
import static com.example.cooing.global.util.DateUtil.getTotalWeekOfMonth;


@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public SecretNoteResponse getSecretNote(CustomUserDetails userDetail, Integer month, Integer week) {
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

        List<Boolean> result1 = evaluateSentenceStructure(answers);
        List<Boolean> result2 = evaluateMeaning(answers);
        List<Boolean> result3 = evaluateMorps(answers);

        // 1단계 데이터
        secretNote.add(result1.get(0));
        secretNote.add(result2.get(0));
        secretNote.add(result2.get(1));
        secretNote.add(result2.get(2));
        secretNote.add(result3.get(0));

        // 2단계 데이터
        secretNote.add(result1.get(1));
        secretNote.add(result2.get(3));
        secretNote.add(result2.get(4));
        secretNote.add(result2.get(5));
        secretNote.add(result3.get(1));
        secretNote.add(result3.get(2));
        secretNote.add(result3.get(3));

        // 3단계 데이터
        secretNote.add(result1.get(2));
        secretNote.add(result2.get(6));
        secretNote.add(result2.get(7));
        secretNote.add(result3.get(4));
        secretNote.add(result3.get(5));

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
