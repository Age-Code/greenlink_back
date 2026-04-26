package com.greenlink.greenlink.dto.admin;

import com.greenlink.greenlink.domain.quest.Quest;
import com.greenlink.greenlink.domain.quest.QuestType;
import com.greenlink.greenlink.domain.quest.ResetCycle;
import com.greenlink.greenlink.domain.quest.TargetType;

public record AdminQuestResponse(
        Long questId,
        String title,
        String description,
        QuestType questType,
        TargetType targetType,
        Integer targetValue,
        Long rewardItemId,
        String rewardItemName,
        Integer rewardQuantity,
        ResetCycle resetCycle,
        Boolean active
) {

    public static AdminQuestResponse from(Quest quest) {
        Long rewardItemId = quest.getRewardItem() == null
                ? null
                : quest.getRewardItem().getId();

        String rewardItemName = quest.getRewardItem() == null
                ? null
                : quest.getRewardItem().getName();

        return new AdminQuestResponse(
                quest.getId(),
                quest.getTitle(),
                quest.getDescription(),
                quest.getQuestType(),
                quest.getTargetType(),
                quest.getTargetValue(),
                rewardItemId,
                rewardItemName,
                quest.getRewardQuantity(),
                quest.getResetCycle(),
                quest.getActive()
        );
    }
}