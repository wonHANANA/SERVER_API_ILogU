package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.HomeResponse;
import com.onehana.server_ilogu.service.HomeService;
import com.onehana.server_ilogu.util.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "홈 화면 정보 출력", description = "이벤트 정보, 가족 성향, 추천 상품, 키워드를 출력한다.")
    @GetMapping
    public BaseResponse<HomeResponse> getHomeInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(homeService.getMyHomeInfo(userDetails.getEmail()));
    }
}
