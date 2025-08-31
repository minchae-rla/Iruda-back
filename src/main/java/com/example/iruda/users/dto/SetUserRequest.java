package com.example.iruda.users.dto;

public record SetUserRequest(
        String name,
        String userPw,
        String department,
        String phone
) {
}
