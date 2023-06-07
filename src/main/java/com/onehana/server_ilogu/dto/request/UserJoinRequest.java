package com.onehana.server_ilogu.dto.request;

import com.onehana.server_ilogu.entity.FamilyRole;
import com.onehana.server_ilogu.entity.FamilyType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserJoinRequest {
    @Email
    @Size(message = "잘못된 이메일 형식입니다.", max = 30)
    private String email;
    @Size(message = "비밀번호는 2자 이상, 20자 이하입니다.", min= 2, max = 20)
    private String password;
    @Size(message = "닉네임은 2자 이상, 10자 이하입니다.", min = 2, max = 10)
    private String nickname;
    private FamilyType familyType;
    private FamilyRole familyRole;
}
