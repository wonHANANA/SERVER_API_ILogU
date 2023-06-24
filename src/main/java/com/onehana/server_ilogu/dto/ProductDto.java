package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.Product;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import com.onehana.server_ilogu.entity.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private ProductType type;
    private BoardCategory category;

    public static ProductDto of(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getType(),
                product.getCategory()
        );
    }
}
