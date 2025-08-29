package com.example.iruda.users.dto;

public record GetUser(
        String userId,
        String name,
        String phone,
        String birth,
        String department
) {
}
