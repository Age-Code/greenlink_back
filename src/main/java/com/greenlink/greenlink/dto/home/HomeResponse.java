package com.greenlink.greenlink.dto.home;

import com.greenlink.greenlink.domain.plant.UserPlant;
import com.greenlink.greenlink.domain.user.User;

import java.time.LocalDate;

public record HomeResponse(
        HomeUserResponse user,
        HomeUserPlantResponse mainUserPlant
) {

    public static HomeResponse of(User user, UserPlant mainUserPlant, LocalDate today) {
        return new HomeResponse(
                HomeUserResponse.from(user),
                HomeUserPlantResponse.from(mainUserPlant, today)
        );
    }
}