package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.BoardDto;
import com.onehana.server_ilogu.entity.BoardCategory;
import com.onehana.server_ilogu.entity.BoardImage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private BoardCategory category;
    private List<BoardImage> boardImages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardResponse of(BoardDto boardDto) {
        return new BoardResponse(
                boardDto.getId(),
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getCategory(),
                boardDto.getBoardImages(),
                boardDto.getCreatedAt(),
                boardDto.getUpdatedAt()
        );
    }
}
