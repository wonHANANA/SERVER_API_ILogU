package com.onehana.server_ilogu.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onehana.server_ilogu.dto.BoardListDto;
import com.onehana.server_ilogu.entity.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BoardListResponse {
    private Long id;
    private String title;
    private String content;
    private String nickName;
    private String userProfileUrl;
    private BoardCategory category;
    @JsonProperty("isLiked")
    private boolean isLiked;
    private int likesCount;
    private int commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BoardImageResponse mainImage;

    public static BoardListResponse of(BoardListDto boardListDto) {
        BoardImageResponse mainImageResponse = (boardListDto.getMainImage() != null)
                ? BoardImageResponse.of(boardListDto.getMainImage()) : null;

        return new BoardListResponse(
                boardListDto.getId(),
                boardListDto.getTitle(),
                boardListDto.getContent(),
                boardListDto.getNickName(),
                boardListDto.getUserProfileUrl(),
                boardListDto.getCategory(),
                boardListDto.isLiked(),
                boardListDto.getLikesCount(),
                boardListDto.getCommentsCount(),
                boardListDto.getCreatedAt(),
                boardListDto.getUpdatedAt(),
                mainImageResponse
        );
    }
}
