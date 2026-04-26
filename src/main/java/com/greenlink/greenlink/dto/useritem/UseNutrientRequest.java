package com.greenlink.greenlink.dto.useritem;

import jakarta.validation.constraints.NotNull;

public record UseNutrientRequest(

        @NotNull(message = "영양제를 사용할 식물 ID는 필수입니다.")
        Long userPlantId
) {
}