package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.*;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BoardListDto {
    private Long id;
    private String title;
    private String content;
    private String nickName;
    private String userProfileUrl;
    private BoardCategory category;
    private Set<String> hashtags;
    private boolean isLiked;
    private int likesCount;
    private int commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BoardImage mainImage;

    public static BoardListDto of(Board board, int likesCount, int commentsCount, boolean isLiked) {
        BoardImage mainImage = (board.getBoardImages() != null && !board.getBoardImages().isEmpty())
                ? board.getBoardImages().get(0) : null;

        Set<String> hashtags = board.getHashtags().stream()
                .map(Hashtag::getHashtagName)
                .collect(Collectors.toSet());

        return new BoardListDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getNickname(),
                board.getUser().getProfileImageUrl(),
                board.getCategory(),
                hashtags,
                isLiked,
                likesCount,
                commentsCount,
                board.getCreatedAt(),
                board.getUpdatedAt(),
                mainImage
        );
    }
}
