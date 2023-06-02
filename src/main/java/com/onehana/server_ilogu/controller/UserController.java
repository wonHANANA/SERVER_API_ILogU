package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.JwtDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.request.UserLoginRequest;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.dto.response.UserJoinResponse;
import com.onehana.server_ilogu.dto.response.UserLoginResponse;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/join")
    public BaseResponse<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserDto userDto = userService.join(request);
        return new BaseResponse<>(UserJoinResponse.of(userDto));
    }

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public BaseResponse<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        JwtDto tokens = userService.login(request);

        UserLoginResponse res = UserLoginResponse.builder()
                .email(request.getEmail())
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .build();

        return new BaseResponse<>(res);
    }

    @Operation(summary = "토큰 재발급", description = "header에 refresh token을 담아서 보낸다.")
    @GetMapping("/token/refresh")
    public BaseResponse<JwtDto> refresh(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            throw new BaseException(BaseResponseStatus.INVALID_HEADER);
        }

        String refreshToken = header.split(" ")[1].trim();
        JwtDto res = userService.refresh(refreshToken);

        return new BaseResponse<>(res);
    }
}
