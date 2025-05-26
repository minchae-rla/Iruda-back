package com.example.iruda.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    // 1. 로그인 URL 반환
    @GetMapping
    public Map<String, String> redirectToKakaoLogin() {
        String redirectUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";

        return Map.of("redirectUrl", redirectUrl);
    }

    // 2. 카카오 로그인 콜백
    @GetMapping("/callback")
    public RedirectView kakaoCallback(@RequestParam String code) throws IOException {
        // 1. access_token 받기
        KakaoTokenDTO tokenDTO = kakaoAuthService.getToken(code);

        // 2. 사용자 정보 받기
        KakaoUserDTO userDTO = kakaoAuthService.getUserInfo(tokenDTO.getAccess_token());

        String nickname = userDTO.getKakao_account().getProfile().getNickname();
        String email = userDTO.getKakao_account().getEmail();

        // 3. 프론트에 필요한 정보 전달 (예: 쿼리 파라미터로 리디렉션)
        String frontendRedirect = "http://localhost:3000/kakao-join?nickname=" + nickname + "&email=" + email;

        return new RedirectView(frontendRedirect);
    }
}
