package com.example.iruda.users.dto;

public record UserRequest(

        String userId,
        String userPw,
        String name,
        String phone,
        String department,
        String provider,
        String providerId
) {
    public record login(
            String userId,
            String userPw) {

    }
}
