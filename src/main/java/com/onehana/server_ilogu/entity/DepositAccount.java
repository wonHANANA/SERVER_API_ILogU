package com.onehana.server_ilogu.entity;

import com.onehana.server_ilogu.dto.DepositAccountDto;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.exception.BaseException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Random;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DepositAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    public void deposit(BigDecimal money) {
        balance = balance.add(money);
    }
    public void withdraw(BigDecimal money) {
        if (balance.compareTo(money) < 0) {
            throw new BaseException(BaseResponseStatus.LACK_OF_BALANCE);
        }
        balance = balance.subtract(money);
    }

    @PrePersist
    public void generateAccountNumber() {
        Random rand = new Random();
        this.accountNumber = String.format("%014d", Math.abs(rand.nextLong()) % (long) Math.pow(10, 14));
        this.balance = BigDecimal.ZERO;
    }

    private DepositAccount(Long id, String accountNumber, BigDecimal balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public static DepositAccount of(DepositAccountDto accountDto) {
        return new DepositAccount(
                accountDto.getId(),
                accountDto.getAccountNumber(),
                accountDto.getBalance()
        );
    }
}
