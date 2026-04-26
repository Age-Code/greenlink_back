package com.greenlink.greenlink.controller;

import com.greenlink.greenlink.common.ApiResponse;
import com.greenlink.greenlink.domain.quest.QuestType;
import com.greenlink.greenlink.dto.quest.QuestDetailResponse;
import com.greenlink.greenlink.dto.quest.QuestListResponse;
import com.greenlink.greenlink.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quests")
public class QuestController {

    private final QuestService questService;

    @GetMapping
    public ApiResponse<List<QuestListResponse>> getQuests(
            @RequestParam(required = false) QuestType questType
    ) {
        List<QuestListResponse> response = questService.getQuests(questType);

        return ApiResponse.success("퀘스트 목록 조회 성공", response);
    }

    @GetMapping("/{questId}")
    public ApiResponse<QuestDetailResponse> getQuest(
            @PathVariable Long questId
    ) {
        QuestDetailResponse response = questService.getQuest(questId);

        return ApiResponse.success("퀘스트 상세 조회 성공", response);
    }
}