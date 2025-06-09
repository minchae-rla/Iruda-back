package com.example.iruda.kakao;

import com.example.iruda.kakao.dto.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    // 1. 카카오 인가 URL 반환
    public String getKakaoAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
    }

    // 2. 인가코드로 액세스 토큰 받고, 유저 정보 요청
    public KakaoUserInfo requestUserInfo(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";

        // 토큰 요청 헤더 및 파라미터 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        // 토큰 요청
        ResponseEntity<TokenResponse> tokenResponse = restTemplate.postForEntity(tokenUri, tokenRequest, TokenResponse.class);
        String accessToken = tokenResponse.getBody().getAccessToken();

        // 사용자 정보 요청 헤더
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);

        HttpEntity<?> userInfoRequest = new HttpEntity<>(userInfoHeaders);

        // 사용자 정보 요청 (자동 매핑)
        ResponseEntity<KakaoUserInfo> userInfoResponse = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                userInfoRequest,
                KakaoUserInfo.class
        );

        return userInfoResponse.getBody();
    }

    // 내부 클래스: 토큰 응답용 DTO
    private static class TokenResponse {
        private String access_token;

        public String getAccessToken() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    }
}
