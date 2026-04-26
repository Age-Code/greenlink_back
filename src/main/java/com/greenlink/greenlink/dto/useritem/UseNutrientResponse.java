package com.greenlink.greenlink.dto.useritem;

import com.greenlink.greenlink.domain.item.UserItem;

public record UseNutrientResponse(
        Long userItemId,
        String itemType,
        String status,
        Long userPlantId
) {

    public static UseNutrientResponse from(UserItem userItem) {
        Long userPlantId = userItem.getUserPlant() == null
                ? null
                : userItem.getUserPlant().getId();

        return new UseNutrientResponse(
                userItem.getId(),
                userItem.getItem().getItemType().name(),
                userItem.getStatus().name(),
                userPlantId
        );
    }
}