package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.Product;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import com.onehana.server_ilogu.entity.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDetailDto {

    private Long id;
    private String name;
    private String description;
    private String managementCompany;
    private ProductType type;
    private BoardCategory category;
    private String profitRate;
    private String imageUrl;
    private String feature;
    private String target;
    private String period;
    private String subscriptionAmount;
    private String minSubscriptionAmount;

    public static ProductDetailDto of(Product product) {
        return new ProductDetailDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getManagementCompany(),
                product.getType(),
                product.getCategory(),
                product.getProfitRate(),
                product.getImageUrl(),
                product.getFeature(),
                product.getTarget(),
                product.getPeriod(),
                product.getSubscriptionAmount(),
                product.getMinSubscriptionAmount()
        );
    }
}
