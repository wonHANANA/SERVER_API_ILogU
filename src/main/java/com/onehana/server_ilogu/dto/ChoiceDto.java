package com.onehana.server_ilogu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceDto implements Serializable {

    private Integer index;
    private MessageDto message;
    @JsonProperty("finish_reason")
    private String finishReason;
}
