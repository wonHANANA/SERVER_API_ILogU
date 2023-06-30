package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String email;
    private String nickname;

    public static UserDto of(User user) {
        return new UserDto(
                user.getEmail(),
                user.getNickname()
        );
    }
}
