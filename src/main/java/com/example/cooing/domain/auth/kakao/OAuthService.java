package com.example.cooing.domain.auth.kakao;

import com.example.cooing.domain.auth.kakao.res.KakaoInfoResponse;
import com.example.cooing.domain.auth.kakao.res.KakaoTokens;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class OAuthService {

    private static final String GRANT_TYPE = "authorization_code";


    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.url.redirect}")
    private String redirectUri;

    @Value("${oauth.kakao.url.redirect-local}")
    private String redirectUriLocal;

    private final RestTemplate restTemplate;

    public String requestAccessToken(String authorizationCode) {
        // isLocal 매개변수를 통해 로컬 또는 배포 주소를 선택합니다.


        String tokenUri = authUrl + "/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        KakaoTokens response = restTemplate.postForObject(tokenUri, request, KakaoTokens.class);
        assert response != null;
        return response.getAccessToken();
    }


    public KakaoInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\",\"kakao_account.gender\",\"kakao_account.age_range\"," +
                "\"kakao_account.name\",\"kakao_account.birthday\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject(url, request, KakaoInfoResponse.class);
    }


}