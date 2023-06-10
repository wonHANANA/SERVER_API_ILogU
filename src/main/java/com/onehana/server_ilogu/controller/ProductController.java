package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.ProductDto;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.entity.Product;
import com.onehana.server_ilogu.entity.enums.ProductType;
import com.onehana.server_ilogu.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "금융 상품 전체 조회", description = "금융 상품을 전체 조회한다.")
    @GetMapping
    public BaseResponse<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.getProducts();
        return new BaseResponse<>(products);
    }

    @Operation(summary = "금융 상품 종류 별 조회", description = "예금-적금 등 종류에 따라 상품을 조회한다.")
    @GetMapping("/type/{type}")
    public BaseResponse<List<ProductDto>> getProductsByType(@PathVariable ProductType type) {
        List<ProductDto> products = productService.getProductsByType(type);
        return new BaseResponse<>(products);
    }
}
