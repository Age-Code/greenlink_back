package com.greenlink.greenlink.dto.useritem;

import com.greenlink.greenlink.domain.item.UserItem;

import java.time.LocalDateTime;

public record UserItemDetailResponse(
        Long userItemId,
        String status,
        Long userPlantId,
        LocalDateTime createdAt
) {

    public static UserItemDetailResponse from(UserItem userItem) {
        Long userPlantId = userItem.getUserPlant() == null
                ? null
                : userItem.getUserPlant().getId();

        return new UserItemDetailResponse(
                userItem.getId(),
                userItem.getStatus().name(),
                userPlantId,
                userItem.getCreatedAt()
        );
    }
}