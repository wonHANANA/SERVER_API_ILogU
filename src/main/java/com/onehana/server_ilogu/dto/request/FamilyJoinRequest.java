package com.onehana.server_ilogu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FamilyJoinRequest {
    @NotBlank
    private String inviteCode;
}
