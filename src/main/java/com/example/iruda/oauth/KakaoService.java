package com.example.iruda.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class KakaoService {

    private final String clientId;
    private final String redirectUri;
    private final WebClient webClientToken;
    private final WebClient webClientUserInfo;

    public KakaoService(
            @Value("${kakao.client_id}") String clientId,
            @Value("${kakao.redirect_uri}") String redirectUri
    ) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;

        this.webClientToken = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        this.webClientUserInfo = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build();
    }

    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDTO tokenResponse = webClientToken
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .bodyToMono(KakaoTokenResponseDTO.class)
                .block();

        return tokenResponse.getAccessToken();
    }

    public KakaoUserInfoDTO getUserInfo(String accessToken) {
        KakaoUserInfoDTO userInfo = webClientUserInfo
                .get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoDTO.class)
                .block();

        return userInfo;
    }
}
