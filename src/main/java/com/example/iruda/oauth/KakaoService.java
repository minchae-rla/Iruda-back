package com.example.iruda.oauth;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KakaoService {

    private final String clientId;
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    public KakaoService(@Value("${kakao.client_id}") String clientId) {
        this.clientId = clientId;
    }

    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDto tokenResponse = WebClient.create(KAUTH_TOKEN_URL_HOST)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("Failed to get Kakao token")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        log.info("Access Token: {}", tokenResponse.getAccessToken());

        return tokenResponse.getAccessToken();
    }

    public KakaoUserInfoDto getUserInfo(String accessToken) {
        KakaoUserInfoDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoDto.class)
                .block();

        log.info("Kakao ID: {}", userInfo.getId());
        log.info("Nickname: {}", userInfo.getKakaoAccount().getProfile().getNickname());

        return userInfo;
    }
}
