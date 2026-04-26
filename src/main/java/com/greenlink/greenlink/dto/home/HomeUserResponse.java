package com.greenlink.greenlink.dto.home;

import com.greenlink.greenlink.domain.user.User;

public record HomeUserResponse(
        Long userId,
        String nickname
) {

    public static HomeUserResponse from(User user) {
        return new HomeUserResponse(
                user.getId(),
                user.getNickname()
        );
    }
}