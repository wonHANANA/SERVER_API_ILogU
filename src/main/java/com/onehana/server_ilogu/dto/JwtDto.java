package com.onehana.server_ilogu.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtDto {
    private String accessToken;
    private String refreshToken;
}
