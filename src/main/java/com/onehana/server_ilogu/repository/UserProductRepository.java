package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProductRepository extends JpaRepository<UserProduct, Long> {

    Optional<UserProduct> findByUserIdAndProductId(Long userId, Long productId);

    List<UserProduct> findByUserId(Long userId);
}
