package com.onehana.server_ilogu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SendMoneyRequest {

    @NotBlank
    private String toUserName;
    @Min(value = 1, message = "송금액은 0원 보다 커야합니다.")
    private BigDecimal money;
}
