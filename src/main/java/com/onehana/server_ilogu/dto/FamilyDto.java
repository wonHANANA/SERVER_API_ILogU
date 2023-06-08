package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.dto.request.FamilyCreateRequest;
import com.onehana.server_ilogu.entity.Family;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyDto {

    private String familyName;
    private String inviteCode;

    public static FamilyDto of(FamilyCreateRequest request) {
        String inviteCode = UUID.randomUUID().toString();

        return new FamilyDto(
                request.getFamilyName(),
                inviteCode
        );
    }

    public static FamilyDto of(Family family) {
        return new FamilyDto(
                family.getFamilyName(),
                family.getInviteCode()
        );
    }
}
