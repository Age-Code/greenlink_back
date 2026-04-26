package com.greenlink.greenlink.dto.userplant;

import com.greenlink.greenlink.domain.plant.UserPlant;

import java.time.LocalDateTime;

public record UserPlantHarvestResponse(
        Long userPlantId,
        Long plantId,
        String plantName,
        String status,
        LocalDateTime harvestedAt
) {

    public static UserPlantHarvestResponse from(UserPlant userPlant) {
        return new UserPlantHarvestResponse(
                userPlant.getId(),
                userPlant.getPlant().getId(),
                userPlant.getPlant().getName(),
                userPlant.getStatus().name(),
                userPlant.getHarvestedAt()
        );
    }
}