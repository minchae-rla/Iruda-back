package com.example.iruda.users.dto;

public record FindPwRequest(
        String userId,
        String name,
        String birth,
        String phone
) {
}
