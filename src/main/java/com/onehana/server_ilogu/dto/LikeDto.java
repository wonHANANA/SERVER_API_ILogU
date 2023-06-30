package com.onehana.server_ilogu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeDto {

    private Integer likes;
    @JsonProperty("isLike")
    private Boolean isLike;

    public static LikeDto of(Integer likes, Boolean isLike) {
        return new LikeDto(
                likes,
                isLike
        );
    }
}
