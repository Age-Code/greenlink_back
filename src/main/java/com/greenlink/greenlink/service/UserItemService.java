package com.greenlink.greenlink.service;

import com.greenlink.greenlink.domain.item.Item;
import com.greenlink.greenlink.domain.item.ItemType;
import com.greenlink.greenlink.domain.item.UserItem;
import com.greenlink.greenlink.domain.item.UserItemStatus;
import com.greenlink.greenlink.domain.plant.UserPlant;
import com.greenlink.greenlink.domain.user.User;
import com.greenlink.greenlink.dto.useritem.EquipPotRequest;
import com.greenlink.greenlink.dto.useritem.EquipPotResponse;
import com.greenlink.greenlink.dto.useritem.UnequipPotResponse;
import com.greenlink.greenlink.dto.useritem.UseNutrientRequest;
import com.greenlink.greenlink.dto.useritem.UseNutrientResponse;
import com.greenlink.greenlink.dto.useritem.UserItemListResponse;
import com.greenlink.greenlink.repository.UserItemRepository;
import com.greenlink.greenlink.repository.UserPlantRepository;
import com.greenlink.greenlink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserItemService {

    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final UserPlantRepository userPlantRepository;

    /**
     * 내 보유 아이템 조회
     *
     * itemType, status는 선택 필터입니다.
     *
     * ownedCount:
     * - OWNED 또는 EQUIPPED 상태
     *
     * usableCount:
     * - OWNED 상태
     *
     * usedCount:
     * - USED 상태
     */
    public List<UserItemListResponse> getUserItems(
            Long userId,
            ItemType itemType,
            UserItemStatus status
    ) {
        User user = findActiveUser(userId);

        List<UserItem> userItems = findUserItems(user, itemType, status);

        Map<Long, List<UserItem>> groupedByItemId = groupByItemId(userItems);

        return groupedByItemId.values()
                .stream()
                .map(groupedItems -> toUserItemListResponse(user, groupedItems))
                .toList();
    }

    /**
     * 화분 장착
     *
     * 처리 흐름:
     * 1. 사용자 조회
     * 2. 장착할 user_item 조회
     * 3. 장착 대상 user_plant 조회
     * 4. item_type = POT 확인
     * 5. status = OWNED 확인
     * 6. 기존 장착 화분 해제
     * 7. 현재 화분 장착
     */
    @Transactional
    public EquipPotResponse equipPot(
            Long userId,
            Long userItemId,
            EquipPotRequest request
    ) {
        User user = findActiveUser(userId);

        UserItem potUserItem = userItemRepository.findByIdAndUserAndDeletedFalse(userItemId, user)
                .orElseThrow(() -> new IllegalArgumentException("장착할 화분 아이템을 찾을 수 없습니다."));

        UserPlant userPlant = userPlantRepository.findByIdAndUserAndDeletedFalse(request.userPlantId(), user)
                .orElseThrow(() -> new IllegalArgumentException("장착 대상 식물을 찾을 수 없습니다."));

        validatePotCanBeEquipped(potUserItem);

        unequipExistingPot(user, userPlant);

        potUserItem.equipPot(userPlant);

        return EquipPotResponse.from(potUserItem);
    }

    /**
     * 화분 장착 해제
     */
    @Transactional
    public UnequipPotResponse unequipPot(
            Long userId,
            Long userItemId
    ) {
        User user = findActiveUser(userId);

        UserItem potUserItem = userItemRepository.findByIdAndUserAndDeletedFalse(userItemId, user)
                .orElseThrow(() -> new IllegalArgumentException("장착 해제할 화분 아이템을 찾을 수 없습니다."));

        validatePotCanBeUnequipped(potUserItem);

        potUserItem.unequipPot();

        return UnequipPotResponse.from(potUserItem);
    }

    /**
     * 영양제 사용
     *
     * 1차 MVP에서는 성장 로직에 영향을 주지 않고,
     * 사용 기록만 남깁니다.
     */
    @Transactional
    public UseNutrientResponse useNutrient(
            Long userId,
            Long userItemId,
            UseNutrientRequest request
    ) {
        User user = findActiveUser(userId);

        UserItem nutrientUserItem = userItemRepository.findByIdAndUserAndDeletedFalse(userItemId, user)
                .orElseThrow(() -> new IllegalArgumentException("사용할 영양제 아이템을 찾을 수 없습니다."));

        UserPlant userPlant = userPlantRepository.findByIdAndUserAndDeletedFalse(request.userPlantId(), user)
                .orElseThrow(() -> new IllegalArgumentException("영양제를 사용할 식물을 찾을 수 없습니다."));

        validateNutrientCanBeUsed(nutrientUserItem);

        nutrientUserItem.useNutrient(userPlant);

        return UseNutrientResponse.from(nutrientUserItem);
    }

    private List<UserItem> findUserItems(
            User user,
            ItemType itemType,
            UserItemStatus status
    ) {
        if (itemType == null && status == null) {
            return userItemRepository.findAllByUserAndDeletedFalse(user);
        }

        if (itemType != null && status == null) {
            return userItemRepository.findAllByUserAndItem_ItemTypeAndDeletedFalse(
                    user,
                    itemType
            );
        }

        if (itemType == null) {
            return userItemRepository.findAllByUserAndStatusAndDeletedFalse(
                    user,
                    status
            );
        }

        return userItemRepository.findAllByUserAndItem_ItemTypeAndStatusAndDeletedFalse(
                user,
                itemType,
                status
        );
    }

    private Map<Long, List<UserItem>> groupByItemId(List<UserItem> userItems) {
        return userItems.stream()
                .collect(
                        LinkedHashMap::new,
                        (map, userItem) -> map.computeIfAbsent(
                                userItem.getItem().getId(),
                                key -> new java.util.ArrayList<>()
                        ).add(userItem),
                        LinkedHashMap::putAll
                );
    }

    private UserItemListResponse toUserItemListResponse(
            User user,
            List<UserItem> groupedItems
    ) {
        Item item = groupedItems.get(0).getItem();

        long ownedCount = userItemRepository.countByUserAndItemAndStatusInAndDeletedFalse(
                user,
                item,
                EnumSet.of(UserItemStatus.OWNED, UserItemStatus.EQUIPPED)
        );

        long usableCount = userItemRepository.countByUserAndItemAndStatusAndDeletedFalse(
                user,
                item,
                UserItemStatus.OWNED
        );

        long usedCount = userItemRepository.countByUserAndItemAndStatusAndDeletedFalse(
                user,
                item,
                UserItemStatus.USED
        );

        return UserItemListResponse.of(
                item,
                ownedCount,
                usableCount,
                usedCount,
                groupedItems
        );
    }

    private void unequipExistingPot(
            User user,
            UserPlant userPlant
    ) {
        userItemRepository
                .findAllByUserAndItem_ItemTypeAndUserPlantAndStatusAndDeletedFalse(
                        user,
                        ItemType.POT,
                        userPlant,
                        UserItemStatus.EQUIPPED
                )
                .forEach(UserItem::unequipPot);
    }

    private void validatePotCanBeEquipped(UserItem userItem) {
        if (userItem.getItem().getItemType() != ItemType.POT) {
            throw new IllegalStateException("화분 아이템만 장착할 수 있습니다.");
        }

        if (userItem.getStatus() != UserItemStatus.OWNED) {
            throw new IllegalStateException("보유 중인 화분만 장착할 수 있습니다.");
        }
    }

    private void validatePotCanBeUnequipped(UserItem userItem) {
        if (userItem.getItem().getItemType() != ItemType.POT) {
            throw new IllegalStateException("화분 아이템만 장착 해제할 수 있습니다.");
        }

        if (userItem.getStatus() != UserItemStatus.EQUIPPED) {
            throw new IllegalStateException("장착 중인 화분만 해제할 수 있습니다.");
        }
    }

    private void validateNutrientCanBeUsed(UserItem userItem) {
        if (userItem.getItem().getItemType() != ItemType.NUTRIENT) {
            throw new IllegalStateException("영양제 아이템만 사용할 수 있습니다.");
        }

        if (userItem.getStatus() != UserItemStatus.OWNED) {
            throw new IllegalStateException("보유 중인 영양제만 사용할 수 있습니다.");
        }
    }

    private User findActiveUser(Long userId) {
        return userRepository.findById(userId)
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}