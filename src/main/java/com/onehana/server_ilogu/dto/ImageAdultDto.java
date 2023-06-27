package com.onehana.server_ilogu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageAdultDto {

    private String imageName;
    @JsonProperty("isAdult")
    private boolean isAdult;
    @JsonProperty("isGory")
    private boolean isGory;
    @JsonProperty("isRacy")
    private boolean isRacy;

    public static ImageAdultDto of(String imageName, boolean isAdult, boolean isGory, boolean isRacy) {
        return new ImageAdultDto(
                imageName,
                isAdult,
                isGory,
                isRacy
        );
    }
}
