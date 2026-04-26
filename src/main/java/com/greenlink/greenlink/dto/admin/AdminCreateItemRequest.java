package com.greenlink.greenlink.dto.admin;

import com.greenlink.greenlink.domain.item.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminCreateItemRequest(

        @NotBlank(message = "아이템 이름은 필수입니다.")
        String name,

        @NotNull(message = "아이템 타입은 필수입니다.")
        ItemType itemType,

        String description,

        String imageUrl,

        Long linkedPlantId
) {
}