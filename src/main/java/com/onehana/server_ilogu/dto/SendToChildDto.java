package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SendToChildDto {

    private int rank;
    private String username;
    private BigDecimal sendToChild;

    private SendToChildDto(String username, BigDecimal sendToChild) {
        this.username = username;
        this.sendToChild = sendToChild;
    }

    public static SendToChildDto of(User user) {
        return new SendToChildDto(
                user.getUsername(),
                user.getDepositAccount().getSendToChild());
    }
}
