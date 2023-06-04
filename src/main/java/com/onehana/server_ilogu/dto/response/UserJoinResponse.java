package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.entity.FamilyRole;
import com.onehana.server_ilogu.entity.FamilyType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserJoinResponse {
    private String email;
    private String password;
    private String nickname;
    private FamilyType familyType;
    private FamilyRole familyRole;

    public static UserJoinResponse of(UserDto userDto) {
        return new UserJoinResponse(
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getNickname(),
                userDto.getFamilyType(),
                userDto.getFamilyRole()
        );
    }
}
