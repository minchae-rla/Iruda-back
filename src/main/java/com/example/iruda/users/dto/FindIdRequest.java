package com.example.iruda.users.dto;


public record FindIdRequest(
        String name,
        String birth,
        String phone
) { }
