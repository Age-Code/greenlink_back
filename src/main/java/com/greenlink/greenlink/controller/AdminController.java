package com.greenlink.greenlink.controller;

import com.greenlink.greenlink.common.ApiResponse;
import com.greenlink.greenlink.dto.admin.AdminCreateItemRequest;
import com.greenlink.greenlink.dto.admin.AdminCreatePlantRequest;
import com.greenlink.greenlink.dto.admin.AdminCreateQuestRequest;
import com.greenlink.greenlink.dto.admin.AdminItemResponse;
import com.greenlink.greenlink.dto.admin.AdminPlantResponse;
import com.greenlink.greenlink.dto.admin.AdminQuestResponse;
import com.greenlink.greenlink.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/plants")
    public ApiResponse<AdminPlantResponse> createPlant(
            @Valid @RequestBody AdminCreatePlantRequest request
    ) {
        AdminPlantResponse response = adminService.createPlant(request);

        return ApiResponse.success("식물이 등록되었습니다.", response);
    }

    @PostMapping("/items")
    public ApiResponse<AdminItemResponse> createItem(
            @Valid @RequestBody AdminCreateItemRequest request
    ) {
        AdminItemResponse response = adminService.createItem(request);

        return ApiResponse.success("아이템이 등록되었습니다.", response);
    }

    @PostMapping("/quests")
    public ApiResponse<AdminQuestResponse> createQuest(
            @Valid @RequestBody AdminCreateQuestRequest request
    ) {
        AdminQuestResponse response = adminService.createQuest(request);

        return ApiResponse.success("퀘스트가 등록되었습니다.", response);
    }
}