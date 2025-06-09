package com.example.iruda.kakao;

import com.example.iruda.kakao.dto.KakaoUserInfo;
import com.example.iruda.users.User;
import com.example.iruda.users.UserRepository;
import com.example.iruda.jwt.JwtGenerator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoOAuthService kakaoOAuthService;
    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;

    @GetMapping("/kakao")
    public ResponseEntity<String> redirectToKakaoAuthorize() {
        String redirectUrl = kakaoOAuthService.getKakaoAuthorizeUrl();
        return ResponseEntity.ok(redirectUrl);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code, HttpServletResponse response) {
        // 1. 인가코드로 사용자 정보 요청
        KakaoUserInfo kakaoUserInfo = kakaoOAuthService.requestUserInfo(code);

        // 2. provider + providerId로 기존 유저 있는지 확인
        Optional<User> existingUser = userRepository.findByProviderAndProviderId(
                kakaoUserInfo.getProvider(), kakaoUserInfo.getProviderId()
        );

        if (existingUser.isPresent()) {
            // 3. 이미 가입된 사용자면 → 로그인 처리 + JWT 발급
            User user = existingUser.get();
            String accessToken = jwtGenerator.generateToken(user.getId()).getAccessToken();
            response.setHeader("Authorization", "Bearer " + accessToken);
            return ResponseEntity.ok("카카오 로그인 성공");
        } else {
            // 4. 신규 사용자면 → 프론트 추가정보 입력 페이지로 리다이렉트
            String tempToken = jwtGenerator.generateTempToken(kakaoUserInfo);
            String redirectUrl = "http://localhost:5173/kakaoSignUp?token=" + tempToken;
            return ResponseEntity.status(302).header("Location", redirectUrl).build();
        }
    }

    @GetMapping("/kakao/session")
    public ResponseEntity<?> getKakaoSessionUser(HttpSession session) {
        KakaoUserInfo kakaoUser = (KakaoUserInfo) session.getAttribute("kakaoUser");
        if (kakaoUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션 없음");
        }
        return ResponseEntity.ok(kakaoUser);  // 프론트에서 provider, providerId 꺼냄
    }
}
