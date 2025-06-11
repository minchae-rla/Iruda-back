package com.example.iruda.api.kakao;

import com.example.iruda.jwt.JwtGenerator;
import com.example.iruda.jwt.JwtTokenDTO;
import com.example.iruda.users.User;
import com.example.iruda.users.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth/kakao")
public class KakaoOAuthController {

    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @GetMapping
    public ResponseEntity<String> getKakaoLoginUrl() {
        String kakaoUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
        return ResponseEntity.ok(kakaoUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&redirect_uri=" + redirectUri
                + "&code=" + code;

        HttpEntity<String> tokenRequest = new HttpEntity<>(body, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                tokenRequest,
                Map.class
        );

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> profileRequest = new HttpEntity<>(profileHeaders);

        ResponseEntity<Map> profileResponse = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                profileRequest,
                Map.class
        );

        Map<String, Object> kakaoAccount = (Map<String, Object>) profileResponse.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String kakaoId = String.valueOf(profileResponse.getBody().get("id"));
        String name = (String) profile.get("nickname");

        Optional<User> optionalUser = userRepository.findByProviderAndProviderId("kakao", kakaoId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            JwtTokenDTO jwtTokenDTO = jwtGenerator.generateToken(user.getId());
            return ResponseEntity.ok(jwtTokenDTO);
        } else {
            try {
                String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
                URI redirectTo = URI.create("http://localhost:5173/kakaoSignUp"
                        + "?name=" + encodedName
                        + "&provider=kakao"
                        + "&providerId=" + kakaoId);
                HttpHeaders redirectHeaders = new HttpHeaders();
                redirectHeaders.setLocation(redirectTo);
                return new ResponseEntity<>(redirectHeaders, HttpStatus.FOUND);
            } catch (Exception e) {
                log.error("Error encoding redirect URI", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redirect error");
            }
        }
    }


}
