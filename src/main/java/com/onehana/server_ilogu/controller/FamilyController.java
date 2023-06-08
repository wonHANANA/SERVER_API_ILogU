package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.FamilyDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.FamilyCreateRequest;
import com.onehana.server_ilogu.dto.request.FamilyJoinRequest;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.FamilyCreateResponse;
import com.onehana.server_ilogu.service.FamilyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/family")
public class FamilyController {

    private final FamilyService familyService;

    @Operation(summary = "가족 생성", description = "새 가족을 생성한다.")
    @PostMapping
    public BaseResponse<FamilyCreateResponse> createFamily(@RequestBody FamilyCreateRequest request,
                                                           Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        FamilyDto familyDto = familyService.createFamily(FamilyDto.of(request), userDto.getEmail());
        return new BaseResponse<>(FamilyCreateResponse.of(familyDto));
    }

    @Operation(summary = "가족에 가입", description = "가족 초대 코드를 이용해 가족에 가입한다.")
    @PostMapping("/join")
    public BaseResponse<String> joinFamily(@RequestBody FamilyJoinRequest request,
                                              Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        String familyName = familyService.joinFamily(request.getInviteCode(), userDto.getEmail());
        return new BaseResponse<>(familyName + "에 등록 되었습니다.");
    }
}
