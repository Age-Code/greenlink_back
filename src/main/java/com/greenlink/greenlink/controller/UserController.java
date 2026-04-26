package com.greenlink.greenlink.controller;

import com.greenlink.greenlink.common.ApiResponse;
import com.greenlink.greenlink.dto.user.UserMeResponse;
import com.greenlink.greenlink.dto.user.UserUpdateNicknameRequest;
import com.greenlink.greenlink.dto.user.UserUpdateNicknameResponse;
import com.greenlink.greenlink.security.CustomUserDetails;
import com.greenlink.greenlink.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserMeResponse> getMe(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserMeResponse response = userService.getMe(userDetails.getUserId());

        return ApiResponse.success("내 정보 조회 성공", response);
    }

    @PatchMapping("/me")
    public ApiResponse<UserUpdateNicknameResponse> updateNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserUpdateNicknameRequest request
    ) {
        UserUpdateNicknameResponse response = userService.updateNickname(
                userDetails.getUserId(),
                request
        );

        return ApiResponse.success("닉네임이 수정되었습니다.", response);
    }
}