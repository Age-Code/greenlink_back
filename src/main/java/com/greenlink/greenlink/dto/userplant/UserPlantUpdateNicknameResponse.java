package com.greenlink.greenlink.dto.userplant;

import com.greenlink.greenlink.domain.plant.UserPlant;

public record UserPlantUpdateNicknameResponse(
        Long userPlantId,
        String nickname
) {

    public static UserPlantUpdateNicknameResponse from(UserPlant userPlant) {
        return new UserPlantUpdateNicknameResponse(
                userPlant.getId(),
                userPlant.getNickname()
        );
    }
}