package com.example.iruda.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class KakaoTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
}