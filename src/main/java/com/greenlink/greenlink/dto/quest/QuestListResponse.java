package com.greenlink.greenlink.dto.quest;

import com.greenlink.greenlink.domain.quest.Quest;

public record QuestListResponse(
        Long questId,
        String title,
        String questType,
        String targetType,
        Integer targetValue,
        String resetCycle,
        Boolean active
) {

    public static QuestListResponse from(Quest quest) {
        return new QuestListResponse(
                quest.getId(),
                quest.getTitle(),
                quest.getQuestType().name(),
                quest.getTargetType().name(),
                quest.getTargetValue(),
                quest.getResetCycle().name(),
                quest.getActive()
        );
    }
}