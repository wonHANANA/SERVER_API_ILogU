package com.onehana.server_ilogu.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onehana.server_ilogu.dto.MessageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChatGptRequest implements Serializable {

    private String model;
    private List<MessageDto> messages;
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    private Double temperature;
    @JsonProperty("top_p")
    private Double topP;

    @Builder
    public ChatGptRequest(String model, List<MessageDto> messages,
                          Integer maxTokens, Double temperature,
                          Double topP) {
        this.model = model;
        this.messages = messages;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.topP = topP;
    }
}
