package com.greenlink.greenlink.controller;

import com.greenlink.greenlink.common.ApiResponse;
import com.greenlink.greenlink.domain.plant.UserPlantStatus;
import com.greenlink.greenlink.dto.userplant.UserPlantCreateRequest;
import com.greenlink.greenlink.dto.userplant.UserPlantCreateResponse;
import com.greenlink.greenlink.dto.userplant.UserPlantDetailResponse;
import com.greenlink.greenlink.dto.userplant.UserPlantListResponse;
import com.greenlink.greenlink.dto.userplant.UserPlantUpdateNicknameRequest;
import com.greenlink.greenlink.dto.userplant.UserPlantUpdateNicknameResponse;
import com.greenlink.greenlink.dto.userplant.UserPlantHarvestResponse;
import com.greenlink.greenlink.security.CustomUserDetails;
import com.greenlink.greenlink.service.UserPlantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-plants")
public class UserPlantController {

    private final UserPlantService userPlantService;

    @PostMapping
    public ApiResponse<UserPlantCreateResponse> createUserPlant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserPlantCreateRequest request
    ) {
        UserPlantCreateResponse response = userPlantService.createUserPlant(userDetails.getUserId(), request);

        return ApiResponse.success("식물이 생성되었습니다.", response);
    }

    @GetMapping
    public ApiResponse<List<UserPlantListResponse>> getUserPlants(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) UserPlantStatus status
    ) {
        List<UserPlantListResponse> response = userPlantService.getUserPlants(userDetails.getUserId(), status);

        return ApiResponse.success("내 식물 목록 조회 성공", response);
    }

    @GetMapping("/{userPlantId}")
    public ApiResponse<UserPlantDetailResponse> getUserPlant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long userPlantId
    ) {
        UserPlantDetailResponse response = userPlantService.getUserPlant(userDetails.getUserId(), userPlantId);

        return ApiResponse.success("내 식물 상세 조회 성공", response);
    }

    @PatchMapping("/{userPlantId}")
    public ApiResponse<UserPlantUpdateNicknameResponse> updateNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long userPlantId,
            @Valid @RequestBody UserPlantUpdateNicknameRequest request
    ) {
        UserPlantUpdateNicknameResponse response = userPlantService.updateNickname(
                userDetails.getUserId(),
                userPlantId,
                request
        );

        return ApiResponse.success("식물 이름이 수정되었습니다.", response);
    }

    @PostMapping("/{userPlantId}/harvest")
    public ApiResponse<UserPlantHarvestResponse> harvestUserPlant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long userPlantId
    ) {
        UserPlantHarvestResponse response = userPlantService.harvestUserPlant(userDetails.getUserId(), userPlantId);

        return ApiResponse.success("식물 수확이 완료되었습니다.", response);
    }
}