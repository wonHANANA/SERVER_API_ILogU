package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.BoardListDto;
import com.onehana.server_ilogu.dto.SendToChildDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.service.BoardService;
import com.onehana.server_ilogu.service.FamilyService;
import com.onehana.server_ilogu.util.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final BoardService boardService;

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

    @Operation(summary = "용돈 랭킹 조회", description = "용돈을 많이 준 가족 순으로 조회한다.")
    @GetMapping("/rank/money")
    public BaseResponse<List<SendToChildDto>> sendToChildRank(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(familyService.sendToChildRank(userDetails.getEmail()));
    }

    @Operation(summary = "가족 게시글 조회", description = "내가 속한 가족이 올린 게시글만 조회한다.")
    @GetMapping("/board")
    public BaseResponse<Page<BoardListDto>> getFamilyBoards(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            Pageable pageable) {
        return new BaseResponse<>(boardService.getFamilyBoards(userDetails.getEmail(), pageable));
    }
}
