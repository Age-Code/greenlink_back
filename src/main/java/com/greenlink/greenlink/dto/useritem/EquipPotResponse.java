package com.greenlink.greenlink.dto.useritem;

import com.greenlink.greenlink.domain.item.UserItem;

public record EquipPotResponse(
        Long userItemId,
        Long itemId,
        String itemName,
        String itemType,
        String status,
        Long userPlantId
) {

    public static EquipPotResponse from(UserItem userItem) {
        Long userPlantId = userItem.getUserPlant() == null
                ? null
                : userItem.getUserPlant().getId();

        return new EquipPotResponse(
                userItem.getId(),
                userItem.getItem().getId(),
                userItem.getItem().getName(),
                userItem.getItem().getItemType().name(),
                userItem.getStatus().name(),
                userPlantId
        );
    }
}