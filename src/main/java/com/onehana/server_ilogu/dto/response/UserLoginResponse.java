package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.entity.enums.FamilyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {
    private String email;
    private FamilyType familyType;
    private String accessToken;
    private String refreshToken;
}
