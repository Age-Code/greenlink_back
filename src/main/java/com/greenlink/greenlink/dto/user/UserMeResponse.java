package com.greenlink.greenlink.dto.user;

import com.greenlink.greenlink.domain.user.User;

import java.time.LocalDateTime;

public record UserMeResponse(
        Long userId,
        String email,
        String nickname,
        String role,
        LocalDateTime createdAt
) {

    public static UserMeResponse from(User user) {
        return new UserMeResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}