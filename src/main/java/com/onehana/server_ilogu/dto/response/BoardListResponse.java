package com.onehana.server_ilogu.dto.response;

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
    private BoardCategory category;
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
                boardListDto.getCategory(),
                boardListDto.getCreatedAt(),
                boardListDto.getUpdatedAt(),
                mainImageResponse
        );
    }
}
