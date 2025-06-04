package com.example.iruda.oauth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/kakao")
public class KakaoOauthController {

    private final KakaoService kakaoService;

    // 클라이언트가 인가 코드 받아서 보낼 때 (기존 post /login은 안 써도 됨)
    @GetMapping("/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code, HttpSession session) {
        log.info("카카오 인가 코드 받음: {}", code);

        // 인가 코드로 액세스 토큰 받아오기
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        // 액세스 토큰으로 사용자 정보 조회
        KakaoUserInfoDto userInfo = kakaoService.getUserInfo(accessToken);

        // 세션에 사용자 정보 저장
        session.setAttribute("userId", userInfo.getId().toString());
        session.setAttribute("userName", userInfo.getKakaoAccount().getProfile().getNickname());

        // 로그인 성공 후 리다이렉트할 페이지 주소
        String redirectUrl = "http://localhost:5173/KakaoSignUp"; // 예시: 프론트 주소

        return ResponseEntity.status(302)  // 302 Redirect
                .header("Location", redirectUrl)
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<KakaoSessionUserDTO> getSessionUser(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");

        if (userId == null || userName == null) {
            return ResponseEntity.notFound().build();
        }

        KakaoSessionUserDTO sessionUser = new KakaoSessionUserDTO(userId, userName);
        return ResponseEntity.ok(sessionUser);
    }
}
