package com.greenlink.greenlink.controller;

import com.greenlink.greenlink.common.ApiResponse;
import com.greenlink.greenlink.domain.item.ItemType;
import com.greenlink.greenlink.domain.item.UserItemStatus;
import com.greenlink.greenlink.dto.useritem.EquipPotRequest;
import com.greenlink.greenlink.dto.useritem.EquipPotResponse;
import com.greenlink.greenlink.dto.useritem.UnequipPotResponse;
import com.greenlink.greenlink.dto.useritem.UseNutrientRequest;
import com.greenlink.greenlink.dto.useritem.UseNutrientResponse;
import com.greenlink.greenlink.dto.useritem.UserItemListResponse;
import com.greenlink.greenlink.security.CustomUserDetails;
import com.greenlink.greenlink.service.UserItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-items")
public class UserItemController {

    private final UserItemService userItemService;

    @GetMapping
    public ApiResponse<List<UserItemListResponse>> getUserItems(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) ItemType itemType,
            @RequestParam(required = false) UserItemStatus status
    ) {
        List<UserItemListResponse> response = userItemService.getUserItems(
                userDetails.getUserId(),
                itemType,
                status
        );

        return ApiResponse.success("내 아이템 조회 성공", response);
    }

    @PostMapping("/{userItemId}/equip-pot")
    public ApiResponse<EquipPotResponse> equipPot(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long userItemId,
            @Valid @RequestBody EquipPotRequest request
    ) {
        EquipPotResponse response = userItemService.equipPot(
                userDetails.getUserId(),
                userItemId,
                request
        );

        return ApiResponse.success("화분이 장착되었습니다.", response);
    }

    @PostMapping("/{userItemId}/unequip-pot")
    public ApiResponse<UnequipPotResponse> unequipPot(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long userItemId
    ) {
        UnequipPotResponse response = userItemService.unequipPot(
                userDetails.getUserId(),
                userItemId
        );

        return ApiResponse.success("화분 장착이 해제되었습니다.", response);
    }

    @PostMapping("/{userItemId}/use-nutrient")
    public ApiResponse<UseNutrientResponse> useNutrient(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long userItemId,
            @Valid @RequestBody UseNutrientRequest request
    ) {
        UseNutrientResponse response = userItemService.useNutrient(
                userDetails.getUserId(),
                userItemId,
                request
        );

        return ApiResponse.success("영양제를 사용했습니다.", response);
    }
}