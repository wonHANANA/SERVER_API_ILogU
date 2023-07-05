package com.onehana.server_ilogu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyPageDto {

    private String nickname;
    private String imageUrl;

    public static MyPageDto of(String nickname, String imageUrl) {
        return new MyPageDto(
                nickname,
                imageUrl
        );
    }
}
