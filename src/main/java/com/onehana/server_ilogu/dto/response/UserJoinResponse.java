package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserJoinResponse {
    private String email;
    private String password;
    private String nickname;

    public static UserJoinResponse of(UserDto userDto) {
        return new UserJoinResponse(
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getNickname()
        );
    }
}
