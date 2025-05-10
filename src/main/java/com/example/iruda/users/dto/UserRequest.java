package com.example.iruda.users.dto;

import java.time.LocalDate;
import java.util.Date;

public record UserRequest(
        String userId,
        String userPw,
        String name,
        String phone,
        String department,
        LocalDate birth,
        boolean privacyAgree,
        String provider,
        String providerId
) {
    public record login(
            String userId,
            String userPw) {

    }
}
