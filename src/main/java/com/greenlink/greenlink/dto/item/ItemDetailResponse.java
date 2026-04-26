package com.greenlink.greenlink.dto.item;

import com.greenlink.greenlink.domain.item.Item;

public record ItemDetailResponse(
        Long itemId,
        String name,
        String itemType,
        String description,
        String imageUrl,
        Long linkedPlantId
) {

    public static ItemDetailResponse from(Item item) {
        Long linkedPlantId = item.getLinkedPlant() == null
                ? null
                : item.getLinkedPlant().getId();

        return new ItemDetailResponse(
                item.getId(),
                item.getName(),
                item.getItemType().name(),
                item.getDescription(),
                item.getImageUrl(),
                linkedPlantId
        );
    }
}