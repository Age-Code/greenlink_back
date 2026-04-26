package com.greenlink.greenlink.dto.collection;

import com.greenlink.greenlink.domain.plant.UserPlant;

import java.time.LocalDateTime;

public record HarvestedPlantResponse(
        Long userPlantId,
        String nickname,
        String imageUrl,
        LocalDateTime plantedAt,
        LocalDateTime harvestedAt
) {

    public static HarvestedPlantResponse from(UserPlant userPlant) {
        return new HarvestedPlantResponse(
                userPlant.getId(),
                userPlant.getNickname(),
                userPlant.getImageUrl(),
                userPlant.getPlantedAt(),
                userPlant.getHarvestedAt()
        );
    }
}