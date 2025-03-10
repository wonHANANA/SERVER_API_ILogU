package com.onehana.server_ilogu.entity;

import com.onehana.server_ilogu.dto.ProductDto;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import com.onehana.server_ilogu.entity.enums.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    private String managementCompany;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private BoardCategory category;

    private String profitRate;
    private String imageUrl;
    private String feature;
    private String target;
    private String period;
    private String subscriptionAmount;
    private String minSubscriptionAmount;

    @OneToMany(mappedBy = "product")
    private List<UserProduct> userProducts = new ArrayList<>();

    private Product(Long id, String name, String description, ProductType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }
    public static Product of(ProductDto productDto) {
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getType()
        );
    }
}
