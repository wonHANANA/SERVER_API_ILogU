package com.onehana.server_ilogu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DepositAccountDto {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
}
