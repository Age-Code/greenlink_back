package com.greenlink.greenlink.dto.userplant;

import com.greenlink.greenlink.domain.plant.UserPlant;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserPlantListResponse(
        Long userPlantId,
        Long plantId,
        String plantName,
        String nickname,
        String status,
        LocalDateTime plantedAt,
        long daysAfterPlanting,
        long remainingDays,
        String imageUrl
) {

    public static UserPlantListResponse from(UserPlant userPlant, LocalDate today) {
        return new UserPlantListResponse(
                userPlant.getId(),
                userPlant.getPlant().getId(),
                userPlant.getPlant().getName(),
                userPlant.getNickname(),
                userPlant.getStatus().name(),
                userPlant.getPlantedAt(),
                userPlant.getDaysAfterPlanting(today),
                userPlant.getRemainingDays(today),
                userPlant.getImageUrl()
        );
    }
}