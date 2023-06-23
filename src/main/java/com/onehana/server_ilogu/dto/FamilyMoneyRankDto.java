package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FamilyMoneyRankDto {

    private String username;
    private String profileUrl;
    private BigDecimal sendToChild;

    public static FamilyMoneyRankDto of(User user) {
        return new FamilyMoneyRankDto(
                user.getUsername(),
                user.getProfileImageUrl(),
                user.getDepositAccount().getSendToChild());
    }
}
