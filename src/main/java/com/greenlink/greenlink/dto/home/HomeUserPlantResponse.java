package com.greenlink.greenlink.dto.home;

import com.greenlink.greenlink.domain.plant.UserPlant;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record HomeUserPlantResponse(
        Long userPlantId,
        String plantName,
        String nickname,
        String status,
        String imageUrl,
        LocalDateTime plantedAt,
        long daysAfterPlanting,
        long remainingDays
) {

    public static HomeUserPlantResponse from(UserPlant userPlant, LocalDate today) {
        if (userPlant == null) {
            return null;
        }

        return new HomeUserPlantResponse(
                userPlant.getId(),
                userPlant.getPlant().getName(),
                userPlant.getNickname(),
                userPlant.getStatus().name(),
                userPlant.getImageUrl(),
                userPlant.getPlantedAt(),
                userPlant.getDaysAfterPlanting(today),
                userPlant.getRemainingDays(today)
        );
    }
}