package com.greenlink.greenlink.service;

import com.greenlink.greenlink.domain.item.Item;
import com.greenlink.greenlink.domain.item.ItemType;
import com.greenlink.greenlink.domain.plant.Plant;
import com.greenlink.greenlink.domain.quest.Quest;
import com.greenlink.greenlink.dto.admin.AdminCreateItemRequest;
import com.greenlink.greenlink.dto.admin.AdminCreatePlantRequest;
import com.greenlink.greenlink.dto.admin.AdminCreateQuestRequest;
import com.greenlink.greenlink.dto.admin.AdminItemResponse;
import com.greenlink.greenlink.dto.admin.AdminPlantResponse;
import com.greenlink.greenlink.dto.admin.AdminQuestResponse;
import com.greenlink.greenlink.repository.ItemRepository;
import com.greenlink.greenlink.repository.PlantRepository;
import com.greenlink.greenlink.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final PlantRepository plantRepository;
    private final ItemRepository itemRepository;
    private final QuestRepository questRepository;

    @Transactional
    public AdminPlantResponse createPlant(AdminCreatePlantRequest request) {
        if (plantRepository.existsByNameAndDeletedFalse(request.name())) {
            throw new IllegalArgumentException("이미 등록된 식물 이름입니다.");
        }

        Plant plant = Plant.create(
                request.name(),
                request.category(),
                request.description(),
                request.imageUrl(),
                request.growthDays()
        );

        Plant savedPlant = plantRepository.save(plant);

        return AdminPlantResponse.from(savedPlant);
    }

    @Transactional
    public AdminItemResponse createItem(AdminCreateItemRequest request) {
        if (itemRepository.existsByNameAndDeletedFalse(request.name())) {
            throw new IllegalArgumentException("이미 등록된 아이템 이름입니다.");
        }

        Item item;

        if (request.itemType() == ItemType.SEED) {
            if (request.linkedPlantId() == null) {
                throw new IllegalArgumentException("씨앗 아이템은 linkedPlantId가 필요합니다.");
            }

            Plant linkedPlant = plantRepository.findByIdAndDeletedFalse(request.linkedPlantId())
                    .orElseThrow(() -> new IllegalArgumentException("연결할 식물을 찾을 수 없습니다."));

            item = Item.createSeed(
                    request.name(),
                    request.description(),
                    request.imageUrl(),
                    linkedPlant
            );
        } else if (request.itemType() == ItemType.POT) {
            item = Item.createPot(
                    request.name(),
                    request.description(),
                    request.imageUrl()
            );
        } else if (request.itemType() == ItemType.NUTRIENT) {
            item = Item.createNutrient(
                    request.name(),
                    request.description(),
                    request.imageUrl()
            );
        } else {
            throw new IllegalArgumentException("지원하지 않는 아이템 타입입니다.");
        }

        Item savedItem = itemRepository.save(item);

        return AdminItemResponse.from(savedItem);
    }

    @Transactional
    public AdminQuestResponse createQuest(AdminCreateQuestRequest request) {
        if (questRepository.existsByTitleAndDeletedFalse(request.title())) {
            throw new IllegalArgumentException("이미 등록된 퀘스트 제목입니다.");
        }

        Item rewardItem = null;

        if (request.rewardItemId() != null) {
            rewardItem = itemRepository.findByIdAndDeletedFalse(request.rewardItemId())
                    .orElseThrow(() -> new IllegalArgumentException("보상 아이템을 찾을 수 없습니다."));
        }

        if (request.rewardQuantity() > 0 && rewardItem == null) {
            throw new IllegalArgumentException("보상 수량이 1개 이상이면 rewardItemId가 필요합니다.");
        }

        Quest quest = Quest.create(
                request.title(),
                request.description(),
                request.questType(),
                request.targetType(),
                request.targetValue(),
                rewardItem,
                request.rewardQuantity(),
                request.resetCycle()
        );

        Quest savedQuest = questRepository.save(quest);

        return AdminQuestResponse.from(savedQuest);
    }
}