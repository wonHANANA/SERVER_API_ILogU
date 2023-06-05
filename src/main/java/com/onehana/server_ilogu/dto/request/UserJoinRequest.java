package com.onehana.server_ilogu.dto.request;

import com.onehana.server_ilogu.entity.FamilyRole;
import com.onehana.server_ilogu.entity.FamilyType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserJoinRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
    private FamilyType familyType;
    private FamilyRole familyRole;
}
