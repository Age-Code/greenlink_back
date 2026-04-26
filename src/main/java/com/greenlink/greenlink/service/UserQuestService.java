package com.greenlink.greenlink.service;

import com.greenlink.greenlink.domain.item.Item;
import com.greenlink.greenlink.domain.item.UserItem;
import com.greenlink.greenlink.domain.quest.Quest;
import com.greenlink.greenlink.domain.quest.QuestType;
import com.greenlink.greenlink.domain.quest.ResetCycle;
import com.greenlink.greenlink.domain.quest.UserQuest;
import com.greenlink.greenlink.domain.quest.UserQuestStatus;
import com.greenlink.greenlink.domain.user.User;
import com.greenlink.greenlink.dto.QuestDto;
import com.greenlink.greenlink.repository.QuestRepository;
import com.greenlink.greenlink.repository.UserItemRepository;
import com.greenlink.greenlink.repository.UserQuestRepository;
import com.greenlink.greenlink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQuestService {

    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final UserQuestRepository userQuestRepository;
    private final UserItemRepository userItemRepository;

    @Transactional
    public List<QuestDto.UserQuestListResDto> getUserQuests(
            Long userId,
            QuestType questType,
            UserQuestStatus status
    ) {
        User user = findActiveUser(userId);

        createCurrentUserQuestsIfNotExists(user);

        List<UserQuest> userQuests = userQuestRepository.findAllByUserAndDeletedFalse(user);

        userQuests.forEach(this::expireIfNeeded);

        return userQuests.stream()
                .filter(userQuest -> questType == null || userQuest.getQuest().getQuestType() == questType)
                .filter(userQuest -> status == null || userQuest.getStatus() == status)
                .map(QuestDto.UserQuestListResDto::from)
                .toList();
    }

    @Transactional
    public QuestDto.UserQuestDetailResDto getUserQuest(Long userId, Long userQuestId) {
        User user = findActiveUser(userId);

        UserQuest userQuest = userQuestRepository.findByIdAndUserAndDeletedFalse(userQuestId, user)
                .orElseThrow(() -> new IllegalArgumentException("내 퀘스트를 찾을 수 없습니다."));

        expireIfNeeded(userQuest);

        return QuestDto.UserQuestDetailResDto.from(userQuest);
    }

    @Transactional
    public QuestDto.UserQuestRewardResDto receiveReward(Long userId, Long userQuestId) {
        User user = findActiveUser(userId);

        UserQuest userQuest = userQuestRepository.findByIdAndUserAndDeletedFalse(userQuestId, user)
                .orElseThrow(() -> new IllegalArgumentException("내 퀘스트를 찾을 수 없습니다."));

        expireIfNeeded(userQuest);

        if (userQuest.getStatus() != UserQuestStatus.ACHIEVABLE) {
            throw new IllegalStateException("아직 보상을 수령할 수 없는 퀘스트입니다.");
        }

        Quest quest = userQuest.getQuest();
        Item rewardItem = quest.getRewardItem();
        Integer rewardQuantity = quest.getRewardQuantity();

        if (rewardItem == null || rewardQuantity == null || rewardQuantity <= 0) {
            throw new IllegalStateException("지급할 보상 아이템이 없습니다.");
        }

        List<UserItem> createdUserItems = new ArrayList<>();

        for (int i = 0; i < rewardQuantity; i++) {
            UserItem userItem = UserItem.createOwned(user, rewardItem);
            createdUserItems.add(userItemRepository.save(userItem));
        }

        userQuest.completeReward();

        return QuestDto.UserQuestRewardResDto.of(
                userQuest,
                rewardItem,
                rewardQuantity,
                createdUserItems
        );
    }

    private void createCurrentUserQuestsIfNotExists(User user) {
        List<Quest> activeQuests = questRepository.findAllByActiveTrueAndDeletedFalse();
        LocalDate today = LocalDate.now();

        for (Quest quest : activeQuests) {
            createCurrentUserQuestIfNotExists(user, quest, today);
        }
    }

    private void createCurrentUserQuestIfNotExists(User user, Quest quest, LocalDate today) {
        if (quest.getQuestType() == QuestType.ACHIEVEMENT || quest.getResetCycle() == ResetCycle.NONE) {
            userQuestRepository.findFirstByUserAndQuestAndDeletedFalse(user, quest)
                    .orElseGet(() -> userQuestRepository.save(
                            UserQuest.create(user, quest, LocalDateTime.now())
                    ));
            return;
        }

        LocalDateTime periodStart = getPeriodStart(quest.getResetCycle(), today);

        userQuestRepository.findByUserAndQuestAndStartedAtAndDeletedFalse(user, quest, periodStart)
                .orElseGet(() -> userQuestRepository.save(
                        UserQuest.create(user, quest, periodStart)
                ));
    }

    private LocalDateTime getPeriodStart(ResetCycle resetCycle, LocalDate today) {
        return switch (resetCycle) {
            case DAILY -> today.atStartOfDay();

            case WEEKLY -> today
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .atStartOfDay();

            case MONTHLY -> today
                    .withDayOfMonth(1)
                    .atStartOfDay();

            case NONE -> today.atStartOfDay();
        };
    }

    private void expireIfNeeded(UserQuest userQuest) {
        if (userQuest.getStatus() != UserQuestStatus.IN_PROGRESS) {
            return;
        }

        if (userQuest.isExpiredBy(LocalDateTime.now())) {
            userQuest.expire();
        }
    }

    private User findActiveUser(Long userId) {
        return userRepository.findById(userId)
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}