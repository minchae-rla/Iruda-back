package com.example.iruda.oauth.dto;

import lombok.Data;

@Data
public class KakaoUserInfo {
    private Long id;
    private KakaoAccount kakao_account;

    public String getNickname() {
        return kakao_account.getProfile().getNickname();
    }

    @Data
    public static class KakaoAccount {
        private Profile profile;

        @Data
        public static class Profile {
            private String nickname;
        }
    }
}
