package com.example.iruda.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoTokenDTO getToken(String code) throws IOException {
        URL url = new URL("https://kauth.kakao.com/oauth/token");

        String params = UriComponentsBuilder.newInstance()
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .build().toUriString().substring(1); // remove '?'

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write(params.getBytes());

        Scanner sc = new Scanner(conn.getInputStream());
        String response = sc.useDelimiter("\\A").next();
        sc.close();

        return objectMapper.readValue(response, KakaoTokenDTO.class);
    }

    public KakaoUserDTO getUserInfo(String accessToken) throws IOException {
        URL url = new URL("https://kapi.kakao.com/v2/user/me");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        Scanner sc = new Scanner(conn.getInputStream());
        String response = sc.useDelimiter("\\A").next();
        sc.close();

        return objectMapper.readValue(response, KakaoUserDTO.class);
    }
}
