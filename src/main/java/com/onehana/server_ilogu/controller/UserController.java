package com.onehana.server_ilogu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onehana.server_ilogu.dto.JwtDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.request.UserLoginRequest;
import com.onehana.server_ilogu.dto.response.*;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.service.FamilyService;
import com.onehana.server_ilogu.service.SmsService;
import com.onehana.server_ilogu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.onehana.server_ilogu.dto.response.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final FamilyService familyService;
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
            throw new BaseException(INVALID_VERIFY_CODE);
        }
    }

    @Operation(summary = "가족코드 일치여부 조회", description = "가족코드가 일치하는지 여부를 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200-00-01", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400-04-09", description = "유효하지 않은 가족 코드입니다."),
    })
    @GetMapping("/join/familyCode/{familyCode}")
    public BaseResponse<Void> isValidFamilyCode(@PathVariable String familyCode) {
        familyService.validFamilyCode(familyCode);
        return new BaseResponse<>(SUCCESS);
    }

    @Operation(summary = "이메일 유효성 조회", description = "이메일이 중복되거나 옳지 않은 형태인지 판별한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200-00-01", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400-04-02", description = "이미 가입된 이메일입니다."),
            @ApiResponse(responseCode = "400-06-01", description = "빈 문자열을 입력하셨습니다."),
    })
    @GetMapping("/join/email/{email}")
    public BaseResponse<Void> isValidEmail(@PathVariable String email) {
        userService.isValidEmail(email);
        return new BaseResponse<>(SUCCESS);
    }

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public BaseResponse<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        return new BaseResponse<>(userService.login(request));
    }

    @Operation(summary = "심플 로그인", description = "간편 비밀번호로 로그인")
    @PostMapping("/simpleLogin")
    public BaseResponse<UserLoginResponse> simpleLogin(@Valid @RequestBody UserLoginRequest request) {
        return new BaseResponse<>(userService.simpleLogin(request));
    }

    @Operation(summary = "일반 페이지 토큰 인증", description = "일반 페이지 접근을 위한 토큰 인증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200-00-01", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400-03-01", description = "Header가 null이거나 형식이 올바르지 않습니다."),
            @ApiResponse(responseCode = "400-03-02", description = "Access 토큰이 유효하지 않습니다."),
            @ApiResponse(responseCode = "400-03-04", description = "Access 토큰이 만료되었습니다.")
    })
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Access Token")
    @GetMapping("/token/pages")
    public BaseResponse<String> showPage() {
        return new BaseResponse<>(SUCCESS);
    }

    @Operation(summary = "토큰 재발급", description = "header에 refresh token을 담아서 보낸다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200-00-01", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400-03-01", description = "Header가 null이거나 형식이 올바르지 않습니다."),
            @ApiResponse(responseCode = "400-03-03", description = "Refresh 토큰이 유효하지 않습니다."),
            @ApiResponse(responseCode = "400-03-05", description = "Refresh 토큰이 만료되었습니다. 로그인이 필요합니다.")
    })
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Refresh Token")
    @GetMapping("/token/refresh")
    public BaseResponse<JwtDto> refresh(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            throw new BaseException(INVALID_HEADER);
        }

        String refreshToken = header.split(" ")[1].trim();
        JwtDto res = userService.refresh(refreshToken);

        return new BaseResponse<>(res);
    }
}
