package com.example.iruda.oauth;

import com.example.iruda.users.User;
import com.example.iruda.users.UserRepository;
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
    private final UserRepository userRepository; // User DB 저장용

    // 1) 카카오 인가 코드 받는 엔드포인트 (GET)
    @GetMapping("/callback")
    public ResponseEntity<Void> kakaoCallback(@RequestParam("code") String code, HttpSession session) {
        log.info("카카오 인가 코드 받음: {}", code);

        // 2) 인가 코드 -> accessToken 요청
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        // 3) accessToken -> 사용자 정보 요청
        KakaoUserInfoDTO userInfo = kakaoService.getUserInfo(accessToken);

        // 4) DB에 providerId로 회원 있는지 확인
        String providerId = userInfo.getId().toString();
        User existingUser = userRepository.findByProviderId(providerId);

        if (existingUser != null) {
            // 이미 가입된 회원이면 바로 세션 등록 후 메인페이지로 redirect
            session.setAttribute("userId", existingUser.getUserId());
            session.setAttribute("userName", existingUser.getName());

            String redirectUrl = "http://localhost:5173/main"; // 로그인 성공 후 메인페이지 주소
            return ResponseEntity.status(302)
                    .header("Location", redirectUrl)
                    .build();
        } else {
            // 신규 회원이면 세션에 카카오정보 임시 저장 후 추가정보 입력 페이지로 redirect
            session.setAttribute("providerId", providerId);
            session.setAttribute("nickname", userInfo.getKakaoAccount().getProfile().getNickname());

            String redirectUrl = "http://localhost:5173/KakaoSignUp"; // 추가정보 입력 페이지
            return ResponseEntity.status(302)
                    .header("Location", redirectUrl)
                    .build();
        }
    }

    // 5) 세션에 저장된 카카오 정보 반환 (프론트가 회원가입 폼에 미리 보여주려고 요청)
    @GetMapping("/me")
    public ResponseEntity<KakaoSessionUserDTO> getSessionUser(HttpSession session) {
        String providerId = (String) session.getAttribute("providerId");
        String nickname = (String) session.getAttribute("nickname");

        if (providerId == null || nickname == null) {
            return ResponseEntity.notFound().build();
        }

        KakaoSessionUserDTO sessionUser = new KakaoSessionUserDTO(providerId, nickname);
        return ResponseEntity.ok(sessionUser);
    }

    // 6) 추가정보 입력받아서 DB 저장 + 세션 초기화 (POST)
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody KakaoSignUpDTO signUpDTO, HttpSession session) {
        String providerId = (String) session.getAttribute("providerId");
        if (providerId == null) {
            return ResponseEntity.badRequest().body("세션이 만료되었습니다. 다시 로그인 해주세요.");
        }

        // providerId는 세션에서 가져오고, userId는 front에서 받은 userId 사용
        User user = new User(
                signUpDTO.getUserId(),
                signUpDTO.getName(),
                signUpDTO.getPhone(),
                signUpDTO.getBirth(),
                signUpDTO.getDepartment(),
                signUpDTO.isPrivacyAgree(),
                "kakao",
                providerId
        );

        userRepository.save(user);

        // 회원가입 완료 후 세션 초기화
        session.invalidate();

        return ResponseEntity.ok("회원가입 완료");
    }

    @GetMapping("/session")
    public ResponseEntity<KakaoSessionUserDTO> getSessionInfo(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");

        if (userId == null || userName == null) {
            return ResponseEntity.status(401).build();
        }

        KakaoSessionUserDTO sessionUser = new KakaoSessionUserDTO(userId, userName);
        return ResponseEntity.ok(sessionUser);
    }
}
