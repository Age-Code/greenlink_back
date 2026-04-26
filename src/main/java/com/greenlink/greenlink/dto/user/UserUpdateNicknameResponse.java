package com.greenlink.greenlink.dto.user;

import com.greenlink.greenlink.domain.user.User;

public record UserUpdateNicknameResponse(
        Long userId,
        String nickname
) {

    public static UserUpdateNicknameResponse from(User user) {
        return new UserUpdateNicknameResponse(
                user.getId(),
                user.getNickname()
        );
    }
}