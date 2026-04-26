package com.greenlink.greenlink.dto.useritem;

import com.greenlink.greenlink.domain.item.Item;
import com.greenlink.greenlink.domain.item.UserItem;

import java.util.List;

public record UserItemListResponse(
        Long itemId,
        String name,
        String itemType,
        String description,
        String imageUrl,
        Long linkedPlantId,
        long ownedCount,
        long usableCount,
        long usedCount,
        List<UserItemDetailResponse> items
) {

    public static UserItemListResponse of(
            Item item,
            long ownedCount,
            long usableCount,
            long usedCount,
            List<UserItem> userItems
    ) {
        Long linkedPlantId = item.getLinkedPlant() == null
                ? null
                : item.getLinkedPlant().getId();

        return new UserItemListResponse(
                item.getId(),
                item.getName(),
                item.getItemType().name(),
                item.getDescription(),
                item.getImageUrl(),
                linkedPlantId,
                ownedCount,
                usableCount,
                usedCount,
                userItems.stream()
                        .map(UserItemDetailResponse::from)
                        .toList()
        );
    }
}