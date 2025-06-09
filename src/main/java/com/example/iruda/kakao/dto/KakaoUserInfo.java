package com.example.iruda.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfo {

    // 카카오에서 받은 유저 고유 ID
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    // 소셜 로그인 플랫폼 고정값 "kakao"
    public String getProvider() {
        return "kakao";
    }

    // providerId로 사용되는 카카오의 유저 고유 ID
    public String getProviderId() {
        return id != null ? id.toString() : null;
    }

    // 닉네임 추출
    public String getNickname() {
        return kakaoAccount != null && kakaoAccount.profile != null
                ? kakaoAccount.profile.nickname
                : null;
    }

    // 이메일 추출
    public String getEmail() {
        return kakaoAccount != null ? kakaoAccount.email : null;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        private Profile profile;
        private String email;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Profile {
            private String nickname;
        }
    }
}
