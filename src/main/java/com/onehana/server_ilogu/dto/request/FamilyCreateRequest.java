package com.onehana.server_ilogu.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FamilyCreateRequest {

    @Size(message = "가족이름은 2자 이상, 16자 이하입니다.", min = 2, max = 16)
    private String familyName;
}
