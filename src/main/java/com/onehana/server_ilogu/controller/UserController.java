package com.onehana.server_ilogu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onehana.server_ilogu.dto.JwtDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.request.UserLoginRequest;
import com.onehana.server_ilogu.dto.response.*;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.service.SmsService;
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

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final SmsService smsService;

    @Operation(summary = "인증 문자 발송", description = "이메일과 전화번호를 받아 인증 메시지를 발송한다. [건당 9원 들어서 호출제한 5초당 1번으로 설정해놓음]")
    @PostMapping("/sendCode")
    public BaseResponse<SmsResponse> sendVerificationCode(@RequestParam String email,
                                                          @RequestParam String phone) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {

        return new BaseResponse<>(smsService.sendVerifySms(email, phone));
    }

    @Operation(summary = "회원가입", description = "회원가입, 인증코드 [onehana] 쓰면 가입가능")
    @PostMapping(value = "/join", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<UserJoinResponse> join(@Valid @RequestPart UserJoinRequest request,
                                               @Nullable @RequestPart("file") MultipartFile file) {

        boolean isValidCode = smsService.isVerifiedCode(request.getEmail(), request.getVerifyCode());
        if (isValidCode || request.getVerifyCode().equals("onehana")) {
            UserDto userDto = userService.join(request, file);
            return new BaseResponse<>(UserJoinResponse.of(userDto));
        } else {
            throw new BaseException(BaseResponseStatus.INVALID_VERIFY_CODE);
        }
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

    @Operation(summary = "일반 페이지 토큰 인증", description = "일반 페이지 접근을 위한 토큰 인증")
    @GetMapping("/token/pages")
    public BaseResponse<String> showPage() {
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
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
