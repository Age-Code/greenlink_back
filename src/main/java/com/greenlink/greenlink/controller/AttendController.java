package com.greenlink.greenlink.controller;

import com.greenlink.greenlink.common.ApiResponse;
import com.greenlink.greenlink.dto.attend.AttendMonthResponse;
import com.greenlink.greenlink.dto.attend.AttendTodayResponse;
import com.greenlink.greenlink.security.CustomUserDetails;
import com.greenlink.greenlink.service.AttendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attends")
public class AttendController {

    private final AttendService attendService;

    @PostMapping("/today")
    public ApiResponse<AttendTodayResponse> attendToday(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AttendTodayResponse response = attendService.attendToday(userDetails.getUserId());

        return ApiResponse.success("출석이 완료되었습니다.", response);
    }

    @GetMapping
    public ApiResponse<AttendMonthResponse> getMyAttends(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        AttendMonthResponse response = attendService.getMyAttends(userDetails.getUserId(), year, month);

        return ApiResponse.success("출석 현황 조회 성공", response);
    }
}