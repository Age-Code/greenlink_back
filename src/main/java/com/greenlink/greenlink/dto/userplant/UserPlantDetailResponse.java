package com.greenlink.greenlink.dto.userplant;

import com.greenlink.greenlink.domain.item.UserItem;
import com.greenlink.greenlink.domain.plant.UserPlant;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserPlantDetailResponse(
        Long userPlantId,
        Long plantId,
        String plantName,
        String nickname,
        String imageUrl,
        String status,
        LocalDateTime plantedAt,
        LocalDateTime harvestedAt,
        long daysAfterPlanting,
        long remainingDays,
        EquippedPotResponse equippedPot
) {

    public static UserPlantDetailResponse of(
            UserPlant userPlant,
            LocalDate today,
            UserItem equippedPot
    ) {
        return new UserPlantDetailResponse(
                userPlant.getId(),
                userPlant.getPlant().getId(),
                userPlant.getPlant().getName(),
                userPlant.getNickname(),
                userPlant.getImageUrl(),
                userPlant.getStatus().name(),
                userPlant.getPlantedAt(),
                userPlant.getHarvestedAt(),
                userPlant.getDaysAfterPlanting(today),
                userPlant.getRemainingDays(today),
                equippedPot == null ? null : EquippedPotResponse.from(equippedPot)
        );
    }
}