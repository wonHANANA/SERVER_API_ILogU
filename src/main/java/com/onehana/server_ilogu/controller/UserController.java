package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.JwtDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.request.UserLoginRequest;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.UserJoinResponse;
import com.onehana.server_ilogu.dto.response.UserLoginResponse;
import com.onehana.server_ilogu.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public BaseResponse<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserDto userDto = userService.join(request);
        return new BaseResponse<>(UserJoinResponse.of(userDto));
    }

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

    @GetMapping("/token/refresh")
    public BaseResponse<JwtDto> refresh(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = header.split(" ")[1].trim();

        JwtDto res = userService.refresh(refreshToken);

        return new BaseResponse<>(res);
    }
}
