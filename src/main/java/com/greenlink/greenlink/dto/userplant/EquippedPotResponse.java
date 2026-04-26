package com.greenlink.greenlink.dto.userplant;

import com.greenlink.greenlink.domain.item.UserItem;

public record EquippedPotResponse(
        Long userItemId,
        Long itemId,
        String name,
        String imageUrl
) {

    public static EquippedPotResponse from(UserItem userItem) {
        return new EquippedPotResponse(
                userItem.getId(),
                userItem.getItem().getId(),
                userItem.getItem().getName(),
                userItem.getItem().getImageUrl()
        );
    }
}