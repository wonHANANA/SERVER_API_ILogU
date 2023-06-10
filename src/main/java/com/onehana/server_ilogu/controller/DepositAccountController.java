package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.DepositAccountDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.DepositAccount;
import com.onehana.server_ilogu.service.DepositAccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class DepositAccountController {

    private final DepositAccountService depositAccountService;

    @Operation(summary = "계좌 생성", description = "계좌를 생성한다.")
    @PostMapping
    public BaseResponse<DepositAccountDto> createDepositAccount(@AuthenticationPrincipal UserDto userDto) {
        DepositAccountDto depositAccountDto = depositAccountService.createDepositAccount(userDto.getEmail());

        return new BaseResponse<>(depositAccountDto);
    }

    @Operation(summary = "계좌 삭제", description = "계좌를 삭제한다.")
    @DeleteMapping("/{accountId}")
    public BaseResponse<Void> deleteDepositAccount(@AuthenticationPrincipal UserDto userDto,
                                                   @PathVariable Long accountId) {
        depositAccountService.deleteDepositAccount(userDto.getEmail(), accountId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "계좌 전체 조회", description = "유저가 가진 계좌를 모두 조회한다.")
    @GetMapping
    public BaseResponse<List<DepositAccountDto>> getAllDepositAccounts(@AuthenticationPrincipal UserDto userDto) {
        List<DepositAccountDto> depositAccounts = depositAccountService.getDepositAccounts(userDto.getEmail());

        return new BaseResponse<>(depositAccounts);
    }
}
