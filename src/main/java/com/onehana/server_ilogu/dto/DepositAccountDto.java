package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.DepositAccount;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DepositAccountDto {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private BigDecimal sendToChild;

    public static DepositAccountDto of(DepositAccount depositAccount) {
        return new DepositAccountDto(
                depositAccount.getId(),
                depositAccount.getAccountNumber(),
                depositAccount.getBalance(),
                depositAccount.getSendToChild()
        );
    }
}
