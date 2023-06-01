package com.onehana.server_ilogu.dto.request;

import com.onehana.server_ilogu.dto.MessageDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class QuestionRequest implements Serializable {
    private List<MessageDto> messages;
}
