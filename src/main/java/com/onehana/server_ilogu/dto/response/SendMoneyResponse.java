package com.onehana.server_ilogu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SendMoneyResponse {
    private String toUserName;
    private BigDecimal sendMoney;
    private BigDecimal remainMoney;
}
