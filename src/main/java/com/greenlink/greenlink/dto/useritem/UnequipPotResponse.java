package com.greenlink.greenlink.dto.useritem;

import com.greenlink.greenlink.domain.item.UserItem;

public record UnequipPotResponse(
        Long userItemId,
        String status,
        Long userPlantId
) {

    public static UnequipPotResponse from(UserItem userItem) {
        Long userPlantId = userItem.getUserPlant() == null
                ? null
                : userItem.getUserPlant().getId();

        return new UnequipPotResponse(
                userItem.getId(),
                userItem.getStatus().name(),
                userPlantId
        );
    }
}