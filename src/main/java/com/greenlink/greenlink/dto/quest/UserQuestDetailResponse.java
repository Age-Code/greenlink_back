package com.greenlink.greenlink.dto.quest;

import com.greenlink.greenlink.domain.quest.UserQuest;

import java.time.LocalDateTime;

public record UserQuestDetailResponse(
        Long userQuestId,
        Long questId,
        String title,
        String description,
        String questType,
        String targetType,
        Integer targetValue,
        Integer progressValue,
        String status,
        LocalDateTime startedAt,
        LocalDateTime expiredAt,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        QuestRewardItemResponse rewardItem,
        Integer rewardQuantity
) {

    public static UserQuestDetailResponse from(UserQuest userQuest) {
        return new UserQuestDetailResponse(
                userQuest.getId(),
                userQuest.getQuest().getId(),
                userQuest.getQuest().getTitle(),
                userQuest.getQuest().getDescription(),
                userQuest.getQuest().getQuestType().name(),
                userQuest.getQuest().getTargetType().name(),
                userQuest.getQuest().getTargetValue(),
                userQuest.getProgressValue(),
                userQuest.getStatus().name(),
                userQuest.getStartedAt(),
                userQuest.getExpiredAt(),
                userQuest.getCreatedAt(),
                userQuest.getModifiedAt(),
                QuestRewardItemResponse.from(userQuest.getQuest().getRewardItem()),
                userQuest.getQuest().getRewardQuantity()
        );
    }
}