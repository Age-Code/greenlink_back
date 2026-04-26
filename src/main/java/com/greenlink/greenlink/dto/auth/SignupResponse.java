package com.greenlink.greenlink.dto.auth;

import com.greenlink.greenlink.domain.item.UserItem;
import com.greenlink.greenlink.domain.user.User;

import java.util.List;

public record SignupResponse(
        Long userId,
        String email,
        String nickname,
        List<GrantedItemResponse> grantedItems
) {

    public static SignupResponse of(User user, List<UserItem> grantedUserItems) {
        return new SignupResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                grantedUserItems.stream()
                        .map(GrantedItemResponse::from)
                        .toList()
        );
    }

    public record GrantedItemResponse(
            Long itemId,
            String name,
            String itemType,
            String status
    ) {

        public static GrantedItemResponse from(UserItem userItem) {
            return new GrantedItemResponse(
                    userItem.getItem().getId(),
                    userItem.getItem().getName(),
                    userItem.getItem().getItemType().name(),
                    userItem.getStatus().name()
            );
        }
    }
}