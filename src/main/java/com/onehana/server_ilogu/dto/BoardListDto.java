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
    private String nickName;
    private String userProfileUrl;
    private BoardCategory category;
    private boolean isLiked;
    private int likesCount;
    private int commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BoardImage mainImage;

    public static BoardListDto of(Board board, int likesCount, int commentsCount, boolean isLiked) {
        BoardImage mainImage = (board.getBoardImages() != null && !board.getBoardImages().isEmpty())
                ? board.getBoardImages().get(0) : null;

        return new BoardListDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getNickname(),
                board.getUser().getProfileImageUrl(),
                board.getCategory(),
                isLiked,
                likesCount,
                commentsCount,
                board.getCreatedAt(),
                board.getUpdatedAt(),
                mainImage
        );
    }
}
