package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.FamilyDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyCreateResponse {

    private String familyName;
    private String inviteCode;

    public static FamilyCreateResponse of(FamilyDto familyDto) {
        return new FamilyCreateResponse(
                familyDto.getFamilyName(),
                familyDto.getInviteCode()
        );
    }
}
