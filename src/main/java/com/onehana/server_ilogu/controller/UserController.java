package com.onehana.server_ilogu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onehana.server_ilogu.dto.JwtDto;
import com.onehana.server_ilogu.dto.MyPageDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.request.UserLoginRequest;
import com.onehana.server_ilogu.dto.response.*;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.service.FamilyService;
import com.onehana.server_ilogu.service.SmsService;
import com.onehana.server_ilogu.service.UserService;
import com.onehana.server_ilogu.util.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.onehana.server_ilogu.dto.response.BaseResponseStatus.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final FamilyService familyService;
    private final SmsService smsService;

    @Operation(summary = "인증 문자 발송", description = "이메일과 전화번호를 받아 인증 메시지를 발송한다. [건당 9원 들어서 호출제한 5초당 1번으로 설정해놓음]")
    @PostMapping("/sendCode")
    public BaseResponse<SmsResponse> sendVerificationCode(@RequestParam String email, @RequestParam String phone)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {

        return new BaseResponse<>(smsService.sendVerifySms(email, phone));
    }

    @Operation(summary = "회원가입", description = "회원가입, 인증코드 [onehana] 쓰면 가입가능, FamilyType은 [PARENTS, OTHERS]")
    @PostMapping(value = "/join")
    public BaseResponse<UserJoinResponse> join(@Valid @RequestBody UserJoinRequest request) {

        boolean isValidCode = smsService.isVerifiedCode(request.getEmail(), request.getVerifyCode());
        if (isValidCode || request.getVerifyCode().equals("onehana")) {
            UserDto userDto = userService.join(request);
            return new BaseResponse<>(UserJoinResponse.of(userDto));
        } else {
            throw new BaseException(INVALID_VERIFY_CODE);
        }
    }

    @Operation(summary = "마이 페이지", description = "마이 페이지 정보를 출력한다. [토큰만 넣으면 됩니다]")
    @GetMapping("/myPage")
    public BaseResponse<MyPageDto> myPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(userService.myPage(userDetails.getEmail()));
    }

    @Operation(summary = "가족코드 일치여부 검사", description = "가족코드가 일치하는지 여부를 확인한다.", tags = "회원가입 유효성 검사")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200-00-01", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400-04-09", description = "유효하지 않은 가족 코드입니다."),
            @ApiResponse(responseCode = "400-06-03", description = "JSON에 null값이나 잘못된 형식이 포함되어 있습니다."),
    })
    @GetMapping("/join/familyCode/{familyCode}")
    public BaseResponse<Void> isValidFamilyCode(@NotBlank @PathVariable String familyCode) {
        familyService.validFamilyCode(familyCode);
        return new BaseResponse<>(SUCCESS);
    }

    @Operation(summary = "이메일 유효성 검사", description = "이메일이 중복되거나 잘못된 형태인지 판별한다.", tags = "회원가입 유효성 검사")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200-00-01", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400-04-02", description = "이미 가입된 이메일입니다."),
            @ApiResponse(responseCode = "400-06-03", description = "JSON에 null값이나 잘못된 형식이 포함되어 있습니다."),
    })
    @GetMapping("/join/email/{email}")
    public BaseResponse<Void> isValidEmail(@NotBlank @Email @PathVariable String email) {
        userService.isDuplicatedEmail(email);
        return new BaseResponse<>(SUCCESS);
    }

    @Operation(summary = "닉네임 유효성 검사", description = "닉네임이 중복되거나 잘못된 형태인지 판별한다.", tags = "회원가입 유효성 검사")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200-00-01", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400-04-01", description = "이미 가입된 닉네임입니다."),
            @ApiResponse(responseCode = "400-06-03", description = "JSON에 null값이나 잘못된 형식이 포함되어 있습니다."),
    })
    @GetMapping("/join/nickname/{nickname}")
    public BaseResponse<Void> isValidNickname(@NotBlank @PathVariable String nickname) {
        userService.isDuplicatedNickname(nickname);
        return new BaseResponse<>(SUCCESS);
    }

    @Operation(summary = "가족 이름 유효성 검사", description = "가족이름이 중복되거나 잘못된 형태인지 판별한다.", tags = "회원가입 유효성 검사")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200-00-01", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400-04-05", description = "중복된 가족 이름입니다."),
            @ApiResponse(responseCode = "400-06-03", description = "JSON에 null값이나 잘못된 형식이 포함되어 있습니다."),
    })
    @GetMapping("/join/familyName/{familyName}")
    public BaseResponse<Void> isValidFamilyName(@NotBlank @PathVariable String familyName) {
        userService.isDuplicatedFamilyName(familyName);
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

    @Operation(summary = "토큰 재발급", description = "header에 refresh token을 담아서 보낸다. Authorization에 access token 대신 refresh token을 넣으세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200-00-01", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400-03-01", description = "Header가 null이거나 형식이 올바르지 않습니다."),
            @ApiResponse(responseCode = "400-03-03", description = "Refresh 토큰이 유효하지 않습니다."),
            @ApiResponse(responseCode = "400-03-05", description = "Refresh 토큰이 만료되었습니다. 로그인이 필요합니다.")
    })
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Refresh Token")
    @PostMapping("/token/refresh")
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
