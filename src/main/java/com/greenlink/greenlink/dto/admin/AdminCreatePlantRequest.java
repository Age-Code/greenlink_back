package com.greenlink.greenlink.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminCreatePlantRequest(

        @NotBlank(message = "식물 이름은 필수입니다.")
        String name,

        @NotBlank(message = "식물 카테고리는 필수입니다.")
        String category,

        String description,

        String imageUrl,

        @NotNull(message = "성장 일수는 필수입니다.")
        @Min(value = 1, message = "성장 일수는 1일 이상이어야 합니다.")
        Integer growthDays
) {
}