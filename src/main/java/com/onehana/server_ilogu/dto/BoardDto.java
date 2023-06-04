package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.BoardCategory;
import com.onehana.server_ilogu.entity.BoardImage;
import com.onehana.server_ilogu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private BoardCategory category;
    private User user;
    private List<BoardImage> boardImages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardDto of(Board board) {
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getCategory(),
                board.getUser(),
                board.getBoardImages(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
