package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.service.FamilyService;
import com.onehana.server_ilogu.util.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.onehana.server_ilogu.dto.response.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/family")
public class FamilyController {

    private final FamilyService familyService;

    @Operation(summary = "우리 가족 구성원 전체 조회", description = "내가 속한 가족 구성원을 전체 조회한다.")
    @GetMapping
    public BaseResponse<List<UserDto>> getFamilyMembers(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(familyService.getFamilyMembers(userDetails.getEmail()));
    }

    @Operation(summary = "아이에게 송금하기", description = "우리 아이에게 돈을 송금한다.")
    @PostMapping("/money/child/{balance}")
    public BaseResponse<List<UserDto>> sendMoneyToChild(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable BigDecimal balance) {
        familyService.sendMoneyToChild(userDetails.getEmail(), balance);

        return new BaseResponse<>(SUCCESS);
    }
}
