package com.greenlink.greenlink.dto.item;

import com.greenlink.greenlink.domain.item.Item;

public record ItemListResponse(
        Long itemId,
        String name,
        String itemType,
        String imageUrl
) {

    public static ItemListResponse from(Item item) {
        return new ItemListResponse(
                item.getId(),
                item.getName(),
                item.getItemType().name(),
                item.getImageUrl()
        );
    }
}