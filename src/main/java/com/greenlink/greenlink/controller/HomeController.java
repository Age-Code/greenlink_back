package com.greenlink.greenlink.controller;

import com.greenlink.greenlink.common.ApiResponse;
import com.greenlink.greenlink.dto.home.HomeResponse;
import com.greenlink.greenlink.security.CustomUserDetails;
import com.greenlink.greenlink.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ApiResponse<HomeResponse> getHome(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        HomeResponse response = homeService.getHome(userDetails.getUserId());

        return ApiResponse.success("홈 데이터 조회 성공", response);
    }
}