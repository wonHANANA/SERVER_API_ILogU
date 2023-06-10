package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.ProductDto;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.entity.Product;
import com.onehana.server_ilogu.entity.enums.ProductType;
import com.onehana.server_ilogu.service.ProductService;
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

    @GetMapping
    public BaseResponse<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.getProducts();
        return new BaseResponse<>(products);
    }

    @GetMapping("/type/{type}")
    public BaseResponse<List<ProductDto>> getProductsByType(@PathVariable ProductType type) {
        List<ProductDto> products = productService.getProductsByType(type);
        return new BaseResponse<>(products);
    }
}
