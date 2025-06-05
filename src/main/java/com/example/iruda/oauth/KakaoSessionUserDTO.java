package com.example.iruda.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoSessionUserDTO {
    private String providerId;  // 카카오 고유 ID (문자열로 변경 가능)
    private String nickname;
}
