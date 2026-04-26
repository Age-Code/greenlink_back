package com.greenlink.greenlink.controller;

import com.greenlink.greenlink.common.ApiResponse;
import com.greenlink.greenlink.dto.AuthDto;
import com.greenlink.greenlink.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<AuthDto.SignupResDto> signup(
            @Valid @RequestBody AuthDto.SignupReqDto request
    ) {
        AuthDto.SignupResDto response = authService.signup(request);

        return ApiResponse.success("회원가입이 완료되었습니다.", response);
    }

    @PostMapping("/login")
    public ApiResponse<AuthDto.LoginResDto> login(
            @Valid @RequestBody AuthDto.LoginReqDto request
    ) {
        AuthDto.LoginResDto response = authService.login(request);

        return ApiResponse.success("로그인에 성공했습니다.", response);
    }
}