package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String content;

    public static BoardResponse of(BoardDto boardDto) {
        return new BoardResponse(
                boardDto.getId(),
                boardDto.getTitle(),
                boardDto.getContent()
        );
    }
}
