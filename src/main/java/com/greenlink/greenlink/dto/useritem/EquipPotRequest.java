package com.greenlink.greenlink.dto.useritem;

import jakarta.validation.constraints.NotNull;

public record EquipPotRequest(

        @NotNull(message = "장착할 식물 ID는 필수입니다.")
        Long userPlantId
) {
}