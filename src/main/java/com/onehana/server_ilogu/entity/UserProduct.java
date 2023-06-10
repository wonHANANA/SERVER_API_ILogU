package com.onehana.server_ilogu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "user_id")
})
public class UserProduct extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private UserProduct(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public static UserProduct of(User user, Product product) {
        return new UserProduct(
                user,
                product
        );
    }
}
