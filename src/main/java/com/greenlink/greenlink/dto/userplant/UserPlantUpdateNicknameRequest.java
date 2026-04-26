package com.greenlink.greenlink.dto.userplant;

import jakarta.validation.constraints.Size;

public record UserPlantUpdateNicknameRequest(

        @Size(max = 50, message = "식물 이름은 50자 이하로 입력해야 합니다.")
        String nickname
) {
}