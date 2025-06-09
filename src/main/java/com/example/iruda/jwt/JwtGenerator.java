package com.example.iruda.jwt;

import com.example.iruda.kakao.dto.KakaoUserInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtGenerator {
    private final Key key;

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
    public static final String GRANT_TYPE = "Bearer";

    // application.properties에서 secret 값을 가져와서 key에 저장
    public JwtGenerator(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JwtTokenDTO generateToken(Long userId) {
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenDTO.builder()
                .grantType(GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateTempToken(KakaoUserInfo kakaoUserInfo) {
        return Jwts.builder()
                .setSubject("TEMP_KAKAO_USER")
                .claim("provider", kakaoUserInfo.getProvider())
                .claim("providerId", kakaoUserInfo.getProviderId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10분 유효
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
