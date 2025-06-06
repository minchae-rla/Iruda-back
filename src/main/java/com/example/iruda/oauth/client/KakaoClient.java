package com.example.iruda.oauth.client;

import com.example.iruda.oauth.dto.KakaoTokenResponse;
import com.example.iruda.oauth.dto.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoClient {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final WebClient webClient = WebClient.create();

    public KakaoTokenResponse getAccessToken(String code) {
        return webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .bodyValue("grant_type=authorization_code&client_id=" + clientId +
                        "&redirect_uri=" + redirectUri +
                        "&code=" + code)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }
}
