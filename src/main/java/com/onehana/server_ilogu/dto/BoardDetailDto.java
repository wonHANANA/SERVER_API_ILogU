package com.onehana.server_ilogu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.onehana.server_ilogu.dto.response.BoardImageResponse;
import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"id", "title", "content", "nickname", "userProfileUrl", "isLiked", "isFamily"})
public class BoardDetailDto {

    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String userProfileUrl;
    private BigDecimal balance;
    private BoardCategory category;
    private Set<String> hashtags;
    private int likesCount;
    private int commentsCount;
    @JsonProperty("isLiked")
    private boolean isLiked;
    @JsonProperty("isFamily")
    private boolean isFamily;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BoardImageResponse> images;
    private Page<CommentDto> comments;

    public static BoardDetailDto of(Board board, int likesCount, int commentsCount, Set<String> hashtags,
                                    boolean isLiked, boolean isFamily, Page<CommentDto> comments) {
        List<BoardImageResponse> images = (board.getBoardImages() != null && !board.getBoardImages().isEmpty())
                ? board.getBoardImages().stream()
                .map(BoardImageResponse::of).toList() : null;

        return new BoardDetailDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getNickname(),
                board.getUser().getProfileImageUrl(),
                board.getBalance(),
                board.getCategory(),
                hashtags,
                likesCount,
                commentsCount,
                isLiked,
                isFamily,
                board.getCreatedAt(),
                board.getUpdatedAt(),
                images,
                comments
        );
    }
}
