package com.greenlink.greenlink.dto.userplant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserPlantCreateRequest(

        @NotNull(message = "사용할 씨앗 아이템 ID는 필수입니다.")
        Long userItemId,

        @Size(max = 50, message = "식물 이름은 50자 이하로 입력해야 합니다.")
        String nickname
) {
}