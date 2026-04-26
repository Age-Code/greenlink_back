package com.greenlink.greenlink.dto.quest;

import com.greenlink.greenlink.domain.quest.Quest;

public record QuestDetailResponse(
        Long questId,
        String title,
        String description,
        String questType,
        String targetType,
        Integer targetValue,
        QuestRewardItemResponse rewardItem,
        Integer rewardQuantity,
        String resetCycle,
        Boolean active
) {

    public static QuestDetailResponse from(Quest quest) {
        return new QuestDetailResponse(
                quest.getId(),
                quest.getTitle(),
                quest.getDescription(),
                quest.getQuestType().name(),
                quest.getTargetType().name(),
                quest.getTargetValue(),
                QuestRewardItemResponse.from(quest.getRewardItem()),
                quest.getRewardQuantity(),
                quest.getResetCycle().name(),
                quest.getActive()
        );
    }
}