package com.onehana.server_ilogu.dto.request;

import lombok.Data;

@Data
public class UserJoinRequest {
    private String email;
    private String password;
    private String nickname;
}
