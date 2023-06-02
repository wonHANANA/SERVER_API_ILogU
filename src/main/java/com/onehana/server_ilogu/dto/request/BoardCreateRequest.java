package com.onehana.server_ilogu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardCreateRequest {
    private String title;
    private String content;
}
