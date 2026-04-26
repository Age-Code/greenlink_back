package com.greenlink.greenlink.dto.auth;

import com.greenlink.greenlink.domain.user.User;

public record LoginUserResponse(
        Long userId,
        String email,
        String nickname,
        String role
) {

    public static LoginUserResponse from(User user) {
        return new LoginUserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole().name()
        );
    }
}