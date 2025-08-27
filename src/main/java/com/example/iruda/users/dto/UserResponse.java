package com.example.iruda.users.dto;

import com.example.iruda.users.User;
import com.example.iruda.users.UserRole;

public record UserResponse(
        Long id,
        String userId,
        String name,
        String phone,
        String birth,
        String department,
        boolean privacyAgree,
        String provider,
        String providerId,
        UserRole role
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getUserId(),
                user.getName(),
                user.getPhone(),
                user.getBirth(),
                user.getDepartment(),
                user.isPrivacyAgree(),
                user.getProvider(),
                user.getProviderId(),
                user.getRole()
        );
    }
}
