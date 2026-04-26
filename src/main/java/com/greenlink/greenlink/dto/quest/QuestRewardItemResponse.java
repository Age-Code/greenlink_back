package com.greenlink.greenlink.dto.quest;

import com.greenlink.greenlink.domain.item.Item;

public record QuestRewardItemResponse(
        Long itemId,
        String name,
        String itemType,
        String imageUrl
) {

    public static QuestRewardItemResponse from(Item item) {
        if (item == null) {
            return null;
        }

        return new QuestRewardItemResponse(
                item.getId(),
                item.getName(),
                item.getItemType().name(),
                item.getImageUrl()
        );
    }
}