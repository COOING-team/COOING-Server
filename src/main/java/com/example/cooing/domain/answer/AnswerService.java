package com.example.cooing.domain.answer;

import static com.example.cooing.global.exception.CustomErrorCode.NO_BABY;
import static com.example.cooing.global.exception.CustomErrorCode.NO_QUESTION;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.cooing.domain.answer.dto.CreateAnswerRequest;
import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.global.entity.Answer;
import com.example.cooing.global.entity.Baby;
import com.example.cooing.global.entity.User;
import com.example.cooing.global.exception.CustomException;
import com.example.cooing.global.repository.AnswerRepository;
import com.example.cooing.global.repository.QuestionRepository;
import com.example.cooing.global.repository.UserRepository;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AnswerService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Value("${openapi.key}")
    private String accessKey;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;


    public String saveFileToStorage(MultipartFile multipartFile) throws IOException {

        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        String fileName = "cooing-" + UUID.randomUUID() + "." + fileExtension;

        String path = "audio/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, path, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, path).toString();
    }

    public String createAnswer(CustomUserDetails userDetails,
        CreateAnswerRequest createAnswerRequest, Long cooingIndex) {

        User user = userRepository.findByEmail(userDetails.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        questionRepository.findByCooingIndex(cooingIndex)
            .orElseThrow(() -> new CustomException(NO_QUESTION));

        List<Map> responseBody = analysisRequest(createAnswerRequest.getAnswerText());

        List<Map> morps = parseMorpAnalysis(responseBody);
        List<Map> words = parseWordAnalysis(responseBody);

        if (user.getBabyList().isEmpty()) {
            throw new CustomException(NO_BABY);
        } else {
            Baby baby = user.getBabyList().get(0); //Todo 베이비 0으로 고정
            try {
                Answer answer = Answer.builder()
                    .babyId(baby.getId()) //Todo 아이수 확장되면 여기 하드코딩 수정하기
                    .cooingIndex(cooingIndex)
                    .fileUrl(createAnswerRequest.getFileUrl())
                    .answerText(createAnswerRequest.getAnswerText())
                    .comment(createAnswerRequest.getComment())
                    .morp(morps)
                    .word(words)
                    .wordCount(words.size())
                    .createAt(LocalDateTime.now())
                    .build();
                answerRepository.save(answer);
            } catch (Exception e) {
                return e.getStackTrace()[0].toString();
            }
        }

        return "등록 성공";
    }

    private List<Map> analysisRequest(String answerText) {
        List<Map> sentences = new ArrayList<>();

        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken";

        Map<String, String> argument = new HashMap<>();
        argument.put("analysis_code", "morp");
        argument.put("text", answerText);

        Map<String, Object> request = new HashMap<>();
        request.put("argument", argument);

        try {
            URL url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", accessKey);

            Gson gson = new Gson();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            Integer responseCode = con.getResponseCode();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();

            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            String responBodyJson = sb.toString();

            if (responseCode != 200) {
                System.out.println("[error] " + responBodyJson);
            }

            Map<String, Object> responseBody = gson.fromJson(responBodyJson, Map.class);
            Map<String, Object> returnObject;

            returnObject = (Map<String, Object>) responseBody.get("return_object");
            sentences = (List<Map>) returnObject.get("sentence");

        } catch (Exception e) {
            e.getStackTrace()[0].toString();
        }
        return sentences;
    }

    private List<Map> parseMorpAnalysis(List<Map> sentences) {
        List<Map> result = new ArrayList<>();

        List<Map> morps = (List<Map>) sentences.get(0).get("morp");

        Long order = 0l;
        for (Map morp : morps) {
            Map<String, String> parsed = new HashMap<>();
            parsed.put("order", order.toString());
            parsed.put("morp", morp.get("lemma").toString());
            parsed.put("type", morp.get("type").toString());
            result.add(parsed);
            order += 1;
        }

        return result;
    }

    private List<Map> parseWordAnalysis(List<Map> sentences) {
        List<Map> result = new ArrayList<>();

        List<Map> words = (List<Map>) sentences.get(0).get("word");

        Long order = 0l;
        for (Map word : words) {
            Map<String, String> parsed = new HashMap<>();
            parsed.put("order", order.toString());
            parsed.put("word", word.get("text").toString());
            result.add(parsed);
            order += 1;
        }

        return result;
    }
}
