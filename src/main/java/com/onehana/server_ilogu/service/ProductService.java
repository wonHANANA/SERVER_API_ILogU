package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.ProductDetailDto;
import com.onehana.server_ilogu.dto.ProductDto;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.Product;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import com.onehana.server_ilogu.entity.enums.ProductType;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDetailDto getProductDetails(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
            new BaseException(BaseResponseStatus.PRODUCTS_NOT_FOUND));

        return ProductDetailDto.of(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByType(ProductType type) {
        return productRepository.findByType(type)
                .stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(BoardCategory category) {
        return productRepository.findByCategoryOrderByType(category)
                .stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
    }
}
