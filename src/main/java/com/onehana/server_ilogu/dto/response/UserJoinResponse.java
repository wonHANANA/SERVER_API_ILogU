package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.entity.enums.FamilyRole;
import com.onehana.server_ilogu.entity.enums.FamilyType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserJoinResponse {
    private String email;
    private String password;
    private String nickname;
    private String username;
    private String phone;
    private FamilyType familyType;
    private FamilyRole familyRole;

    public static UserJoinResponse of(UserDto userDto) {
        return new UserJoinResponse(
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getNickname(),
                userDto.getUsername(),
                userDto.getPhone(),
                userDto.getFamilyType(),
                userDto.getFamilyRole()
        );
    }
}
