package com.greenlink.greenlink.controller;

import com.greenlink.greenlink.common.ApiResponse;
import com.greenlink.greenlink.dto.plant.PlantDetailResponse;
import com.greenlink.greenlink.dto.plant.PlantListResponse;
import com.greenlink.greenlink.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;

    @GetMapping
    public ApiResponse<List<PlantListResponse>> getPlants() {
        List<PlantListResponse> response = plantService.getPlants();

        return ApiResponse.success("식물 목록 조회 성공", response);
    }

    @GetMapping("/{plantId}")
    public ApiResponse<PlantDetailResponse> getPlant(
            @PathVariable Long plantId
    ) {
        PlantDetailResponse response = plantService.getPlant(plantId);

        return ApiResponse.success("식물 상세 조회 성공", response);
    }
}