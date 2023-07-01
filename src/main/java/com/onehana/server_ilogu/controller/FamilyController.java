package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.BoardListDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.FamilyHomeResponse;
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

    @Operation(summary = "우리 가족 메인 화면", description = "우리 가족 탭 메인 화면 출력")
    @GetMapping
    public BaseResponse<FamilyHomeResponse> mainPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     Pageable pageable) {
        return new BaseResponse<>(familyService.mainPage(userDetails.getEmail(), pageable));
    }

    @Operation(summary = "우리 가족 구성원 전체 조회", description = "내가 속한 가족 구성원을 전체 조회한다.")
    @GetMapping("/members")
    public BaseResponse<List<UserDto>> getFamilyMembers(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(familyService.getFamilyMembers(userDetails.getEmail()));
    }

    @Operation(summary = "피드글을 통해 아이에게 송금하기", description = "우리 아이에게 돈을 송금한다.")
    @PostMapping("/board/{boardId}/balance/{balance}")
    public BaseResponse<BigDecimal> sendMoneyToChild(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable Long boardId, @PathVariable BigDecimal balance) {
        return new BaseResponse<>(familyService.sendMoneyToChild(userDetails.getEmail(), boardId, balance));
    }

    @Operation(summary = "가족 게시글 조회", description = "내가 속한 가족이 올린 게시글만 조회한다.")
    @GetMapping("/board")
    public BaseResponse<Page<BoardListDto>> getFamilyBoards(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            Pageable pageable) {
        return new BaseResponse<>(boardService.getFamilyBoards(userDetails.getEmail(), pageable));
    }

    @Operation(summary = "아이가 받은 용돈 조회", description = "우리 가족이 아이에게 준 모든 돈을 조회한다.")
    @GetMapping("/child/balance")
    public BaseResponse<BigDecimal> getChildBalance(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(familyService.getChildBalance(userDetails.getEmail()));
    }
}
