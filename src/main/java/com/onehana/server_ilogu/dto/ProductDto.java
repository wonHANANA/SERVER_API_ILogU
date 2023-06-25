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
    private String managementCompany;
    private ProductType type;
    private BoardCategory category;
    private String profitRate;
    private String imageUrl;

    public static ProductDto of(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getManagementCompany(),
                product.getType(),
                product.getCategory(),
                product.getProfitRate(),
                product.getImageUrl()
        );
    }
}
