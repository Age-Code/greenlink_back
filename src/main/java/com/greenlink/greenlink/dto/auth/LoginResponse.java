package com.greenlink.greenlink.dto.auth;

import com.greenlink.greenlink.domain.user.User;

public record LoginResponse(
        String accessToken,
        LoginUserResponse user
) {

    public static LoginResponse of(String accessToken, User user) {
        return new LoginResponse(
                accessToken,
                LoginUserResponse.from(user)
        );
    }
}