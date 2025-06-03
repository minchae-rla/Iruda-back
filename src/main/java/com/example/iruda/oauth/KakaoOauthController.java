package com.example.iruda.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoOauthController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<Void> kakaoOauthCallback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoDto userInfo = kakaoService.getUserInfo(accessToken);

        String kakaoId = userInfo.getId().toString();
        String nickname = userInfo.getKakaoAccount().getProfile().getNickname();

        // 프론트 회원가입 페이지로 리다이렉트
        String redirectUrl = "/kakaoSignUp?nickname=" + nickname + "&kakaoId=" + kakaoId;

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl)
                .build();
    }
}
