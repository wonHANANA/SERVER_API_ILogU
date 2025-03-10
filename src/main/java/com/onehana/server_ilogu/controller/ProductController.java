package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.ProductDetailDto;
import com.onehana.server_ilogu.dto.ProductDto;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.enums.ProductType;
import com.onehana.server_ilogu.service.ProductService;
import com.onehana.server_ilogu.service.UserProductService;
import com.onehana.server_ilogu.util.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final UserProductService userProductService;

    @Operation(summary = "하나금융 상품 전체 조회", description = "하나 금융 상품을 전체 조회한다.")
    @GetMapping
    public BaseResponse<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.getProducts();
        return new BaseResponse<>(products);
    }

    @Operation(summary = "하나금융 상품정보 상세 조회", description = "금융 상품의 상세 정보를 조회한다.")
    @GetMapping("/productId/{productId}")
    public BaseResponse<ProductDetailDto> getProductDetails(@PathVariable Long productId) {
        ProductDetailDto product = productService.getProductDetails(productId);
        return new BaseResponse<>(product);
    }

    @Operation(summary = "하나금융 상품 종류 별 조회 ProductType = [DEPOSIT, INSTALLMENT_SAVINGS, LOANS, FUND, INSURANCE, ETF]", description = "하나 예금-적금 등 종류에 따라 상품을 조회한다.")
    @GetMapping("/type/{type}")
    public BaseResponse<List<ProductDto>> getProductsByType(@PathVariable ProductType type) {
        List<ProductDto> products = productService.getProductsByType(type);
        return new BaseResponse<>(products);
    }

    @Operation(summary = "유저 금융 상품 등록", description = "유저가 금융 상품 중 하나를 등록한다.")
    @PostMapping("/{productId}")
    public BaseResponse<Void> enrollProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long productId) {
        userProductService.enrollProduct(userDetails.getEmail(), productId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "유저 금융 상품 취소", description = "유저가 가진 금융 상품 중 하나를 취소한다.")
    @DeleteMapping("/{productId}")
    public BaseResponse<Void> cancelProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long productId) {
        userProductService.cancelProduct(userDetails.getEmail(), productId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "내가 가진 금융 상품 조회", description = "내가 현재 가지고 있는 금융 상품을 전체 조회한다.")
    @GetMapping("/my")
    public BaseResponse<List<ProductDto>> getUserProducts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ProductDto> products = userProductService.getUserProducts(userDetails.getEmail());
        return new BaseResponse<>(products);
    }
}
