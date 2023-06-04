package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.BoardCategory;
import com.onehana.server_ilogu.entity.BoardImage;
import com.onehana.server_ilogu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BoardListDto {
    private Long id;
    private String title;
    private String content;
    private BoardCategory category;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BoardImage mainImage;


    public static BoardListDto of(Board board) {
        BoardImage mainImage = (board.getBoardImages() != null && !board.getBoardImages().isEmpty())
                ? board.getBoardImages().get(0) : null;

        return new BoardListDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getCategory(),
                board.getUser(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                mainImage
        );
    }
}
