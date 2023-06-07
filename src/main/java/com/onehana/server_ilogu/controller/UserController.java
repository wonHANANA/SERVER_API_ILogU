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
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping(value = "/join", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<UserJoinResponse> join(@Valid @RequestPart UserJoinRequest request,
                                               @Nullable @RequestPart("file") MultipartFile file) {

        UserDto userDto = userService.join(request, file);
        return new BaseResponse<>(UserJoinResponse.of(userDto));
    }

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public BaseResponse<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
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
