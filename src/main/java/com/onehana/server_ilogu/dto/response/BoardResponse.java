package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.BoardDto;
import com.onehana.server_ilogu.entity.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private BoardCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardResponse of(BoardDto boardDto) {
        return new BoardResponse(
                boardDto.getId(),
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getCategory(),
                boardDto.getCreatedAt(),
                boardDto.getUpdatedAt()
        );
    }
}
