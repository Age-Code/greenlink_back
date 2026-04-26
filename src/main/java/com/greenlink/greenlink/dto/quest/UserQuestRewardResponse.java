package com.greenlink.greenlink.dto.quest;

import com.greenlink.greenlink.domain.item.Item;
import com.greenlink.greenlink.domain.item.UserItem;
import com.greenlink.greenlink.domain.quest.UserQuest;

import java.util.List;

public record UserQuestRewardResponse(
        Long userQuestId,
        String status,
        RewardResponse reward,
        List<CreatedUserItemResponse> createdUserItems
) {

    public static UserQuestRewardResponse of(
            UserQuest userQuest,
            Item rewardItem,
            Integer quantity,
            List<UserItem> createdUserItems
    ) {
        return new UserQuestRewardResponse(
                userQuest.getId(),
                userQuest.getStatus().name(),
                RewardResponse.of(rewardItem, quantity),
                createdUserItems.stream()
                        .map(CreatedUserItemResponse::from)
                        .toList()
        );
    }

    public record RewardResponse(
            Long itemId,
            String itemName,
            String itemType,
            Integer quantity
    ) {

        public static RewardResponse of(Item item, Integer quantity) {
            return new RewardResponse(
                    item.getId(),
                    item.getName(),
                    item.getItemType().name(),
                    quantity
            );
        }
    }

    public record CreatedUserItemResponse(
            Long userItemId,
            Long itemId,
            String status
    ) {

        public static CreatedUserItemResponse from(UserItem userItem) {
            return new CreatedUserItemResponse(
                    userItem.getId(),
                    userItem.getItem().getId(),
                    userItem.getStatus().name()
            );
        }
    }
}