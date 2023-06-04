package com.onehana.server_ilogu.dto.request;

import com.onehana.server_ilogu.entity.FamilyRole;
import com.onehana.server_ilogu.entity.FamilyType;
import lombok.Data;

@Data
public class UserJoinRequest {
    private String email;
    private String password;
    private String nickname;
    private FamilyType familyType;
    private FamilyRole familyRole;
}
