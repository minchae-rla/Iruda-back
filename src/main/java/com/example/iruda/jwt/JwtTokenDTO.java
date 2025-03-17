package com.example.iruda.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtTokenDTO {
    private String grantType;
    //서버에 접근하기 위한 토큰(세션과 비슷)
    private String accessToken;
    //토큰이 만료되었을시 새 토큰을 받기위한 토큰
    private String refreshToken;
}
