package com.greenlink.greenlink.dto.quest;

import com.greenlink.greenlink.domain.quest.UserQuest;

import java.time.LocalDateTime;

public record UserQuestListResponse(
        Long userQuestId,
        Long questId,
        String title,
        String questType,
        String targetType,
        Integer targetValue,
        Integer progressValue,
        String status,
        LocalDateTime startedAt,
        LocalDateTime expiredAt
) {

    public static UserQuestListResponse from(UserQuest userQuest) {
        return new UserQuestListResponse(
                userQuest.getId(),
                userQuest.getQuest().getId(),
                userQuest.getQuest().getTitle(),
                userQuest.getQuest().getQuestType().name(),
                userQuest.getQuest().getTargetType().name(),
                userQuest.getQuest().getTargetValue(),
                userQuest.getProgressValue(),
                userQuest.getStatus().name(),
                userQuest.getStartedAt(),
                userQuest.getExpiredAt()
        );
    }
}