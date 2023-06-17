package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private String simplePassword;
    private String nickname;
    private String username;
    private String phone;
    private UserRole userRole;

    public static UserDto of(UserJoinRequest request) {
        return new UserDto(
                request.getEmail(),
                request.getPassword(),
                request.getSimplePassword(),
                request.getNickname(),
                request.getUsername(),
                request.getPhone(),
                UserRole.USER_ROLE
        );
    }

    public static UserDto of(User user) {
        return new UserDto(
                user.getEmail(),
                user.getPassword(),
                user.getSimplePassword(),
                user.getNickname(),
                user.getUsername(),
                user.getPhone(),
                user.getUserRole()
        );
    }
}
