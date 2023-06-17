package com.onehana.server_ilogu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Child {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String birth;
    private BigDecimal balance;

    @OneToOne(mappedBy = "child")
    private Family family;

    public void deposit(BigDecimal money) {
        balance = balance.add(money);
    }

    private Child(String name, String birth, BigDecimal balance) {
        this.name = name;
        this.birth = birth;
        this.balance = balance;
    }

    public static Child of(String name, String birth, BigDecimal balance) {
        return new Child(
                name,
                birth,
                balance
        );
    }
}
