package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.ProductDto;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.Product;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.entity.UserProduct;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.ProductRepository;
import com.onehana.server_ilogu.repository.UserProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProductService {

    private final UserService userService;
    private final ProductRepository productRepository;
    private final UserProductRepository userProductRepository;

    public void enrollProduct(String email, Long productId) {
        User user = userService.getUserOrException(email);
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.PRODUCTS_NOT_FOUND));

        userProductRepository.save(UserProduct.of(user, product));
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getUserProducts(String email) {
        User user = userService.getUserOrException(email);

        return userProductRepository.findByUserId(user.getId())
                .stream()
                .map(UserProduct::getProduct)
                .map(ProductDto::of)
                .collect(Collectors.toList());
    }

    public void cancelProduct(String email, Long productId) {
        User user = userService.getUserOrException(email);
        UserProduct userProduct = userProductRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCTS_NOT_FOUND));

        if (userProduct.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_PERMISSION);
        }
        userProductRepository.delete(userProduct);
    }
}
