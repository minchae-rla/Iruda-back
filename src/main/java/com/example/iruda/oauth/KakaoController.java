package com.example.iruda.oauth;

import com.example.iruda.jwt.JwtTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<JwtTokenDTO> kakaoCallback(@RequestParam("code") String code) {
        JwtTokenDTO jwt = kakaoService.kakaoLoginOrRegister(code);
        return ResponseEntity.ok(jwt);
    }
}
