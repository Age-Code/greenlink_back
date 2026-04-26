package com.greenlink.greenlink.dto.userplant;

import com.greenlink.greenlink.domain.plant.UserPlant;

import java.time.LocalDateTime;

public record UserPlantCreateResponse(
        Long userPlantId,
        Long plantId,
        String plantName,
        String nickname,
        String status,
        LocalDateTime plantedAt,
        LocalDateTime expectedHarvestableAt
) {

    public static UserPlantCreateResponse from(UserPlant userPlant) {
        LocalDateTime expectedHarvestableAt = userPlant.getPlantedAt()
                .plusDays(userPlant.getPlant().getGrowthDays());

        return new UserPlantCreateResponse(
                userPlant.getId(),
                userPlant.getPlant().getId(),
                userPlant.getPlant().getName(),
                userPlant.getNickname(),
                userPlant.getStatus().name(),
                userPlant.getPlantedAt(),
                expectedHarvestableAt
        );
    }
}