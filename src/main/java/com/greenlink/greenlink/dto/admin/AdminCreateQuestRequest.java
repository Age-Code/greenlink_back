package com.greenlink.greenlink.dto.admin;

import com.greenlink.greenlink.domain.quest.QuestType;
import com.greenlink.greenlink.domain.quest.ResetCycle;
import com.greenlink.greenlink.domain.quest.TargetType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminCreateQuestRequest(

        @NotBlank(message = "퀘스트 제목은 필수입니다.")
        String title,

        String description,

        @NotNull(message = "퀘스트 타입은 필수입니다.")
        QuestType questType,

        @NotNull(message = "목표 타입은 필수입니다.")
        TargetType targetType,

        @NotNull(message = "목표 수치는 필수입니다.")
        @Min(value = 1, message = "목표 수치는 1 이상이어야 합니다.")
        Integer targetValue,

        Long rewardItemId,

        @NotNull(message = "보상 수량은 필수입니다.")
        @Min(value = 0, message = "보상 수량은 0 이상이어야 합니다.")
        Integer rewardQuantity,

        @NotNull(message = "반복 주기는 필수입니다.")
        ResetCycle resetCycle
) {
}