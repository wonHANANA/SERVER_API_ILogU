package com.onehana.server_ilogu.entity;

import com.onehana.server_ilogu.entity.enums.ProductType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @OneToMany(mappedBy = "product")
    private List<UserProduct> userProducts = new ArrayList<>();
}
