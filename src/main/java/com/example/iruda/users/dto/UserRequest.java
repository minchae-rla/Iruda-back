package com.example.iruda.users.dto;

import java.time.LocalDate;

public record UserRequest(
        String userId,
        String userPw,
        String name,
        String phone,
        String department,
        String birth,
        boolean privacyAgree,
        String provider,
        String providerId
) {
    public record login(
            String userId,
            String userPw) {

    }
}


