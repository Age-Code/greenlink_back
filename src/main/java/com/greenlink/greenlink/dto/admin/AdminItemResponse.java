package com.greenlink.greenlink.dto.admin;

import com.greenlink.greenlink.domain.item.Item;
import com.greenlink.greenlink.domain.item.ItemType;

public record AdminItemResponse(
        Long itemId,
        String name,
        ItemType itemType,
        String description,
        String imageUrl,
        Long linkedPlantId
) {

    public static AdminItemResponse from(Item item) {
        Long linkedPlantId = item.getLinkedPlant() == null
                ? null
                : item.getLinkedPlant().getId();

        return new AdminItemResponse(
                item.getId(),
                item.getName(),
                item.getItemType(),
                item.getDescription(),
                item.getImageUrl(),
                linkedPlantId
        );
    }
}