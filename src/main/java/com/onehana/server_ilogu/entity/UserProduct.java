package com.onehana.server_ilogu.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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
}
