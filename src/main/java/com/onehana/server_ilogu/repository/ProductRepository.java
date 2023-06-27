package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.Product;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import com.onehana.server_ilogu.entity.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByType(ProductType type);
    List<Product> findByCategoryOrderByType(BoardCategory category);
}
