package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.dto.request.BoardCreateRequest;
import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import com.onehana.server_ilogu.entity.Hashtag;
import com.onehana.server_ilogu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private BoardCategory category;
    private Set<Hashtag> hashtags;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoardDto(String title, String content, BoardCategory category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public static BoardDto of(Board board) {
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getCategory(),
                board.getHashtags(),
                board.getUser(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }

    public static BoardDto of(BoardCreateRequest request) {
        return new BoardDto(
                request.getTitle(),
                request.getContent(),
                request.getCategory()
        );
    }
}
