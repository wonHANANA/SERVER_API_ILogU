package com.onehana.server_ilogu.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {
    private String email;
    private String accessToken;
    private String refreshToken;
}
