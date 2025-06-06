package com.example.iruda.oauth.dto;

import lombok.Data;

@Data
public class KakaoTokenResponse {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Long expires_in;
    private String scope;
}
