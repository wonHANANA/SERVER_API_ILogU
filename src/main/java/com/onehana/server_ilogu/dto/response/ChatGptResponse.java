package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.ChoiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatGptResponse implements Serializable {

    private String id;
    private String object;
    private LocalDate created;
    private String model;
    private List<ChoiceDto> choices;

    @Builder
    public ChatGptResponse(String id, String object,
                           LocalDate created, String model,
                           List<ChoiceDto> choices) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choices = choices;
    }
}
