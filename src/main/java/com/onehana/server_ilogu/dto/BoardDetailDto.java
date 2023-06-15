package com.onehana.server_ilogu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.onehana.server_ilogu.dto.response.BoardImageResponse;
import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.Hashtag;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"id", "title", "content", "likesCount", "commentsCount", "isLiked"})
public class BoardDetailDto {

    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String userProfileUrl;
    private BoardCategory category;
    private Set<String> hashtags;
    private int likesCount;
    private int commentsCount;
    @JsonProperty("isLiked")
    private boolean isLiked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BoardImageResponse> images;
    private Page<CommentDto> comments;

    public static BoardDetailDto of(Board board, int likesCount, int commentsCount,
                                    boolean isLiked, Page<CommentDto> comments) {
        List<BoardImageResponse> images = (board.getBoardImages() != null && !board.getBoardImages().isEmpty())
                ? board.getBoardImages().stream()
                .map(BoardImageResponse::of).toList() : null;

        Set<String> hashtags = board.getHashtags().stream()
                .map(Hashtag::getHashtagName)
                .collect(Collectors.toSet());

        return new BoardDetailDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getNickname(),
                board.getUser().getProfileImageUrl(),
                board.getCategory(),
                hashtags,
                likesCount,
                commentsCount,
                isLiked,
                board.getCreatedAt(),
                board.getUpdatedAt(),
                images,
                comments
        );
    }
}
