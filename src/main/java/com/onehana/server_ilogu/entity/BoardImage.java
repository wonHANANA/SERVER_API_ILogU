package com.onehana.server_ilogu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onehana.server_ilogu.dto.BoardImageDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardImage extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;

    public static BoardImage from(BoardImageDto boardImageDto, Board board) {
        BoardImage boardImage = new BoardImage();
        boardImage.setOriginalFileName(boardImageDto.getOriginalFileName());
        boardImage.setUploadFileName(boardImageDto.getUploadFileName());
        boardImage.setUploadFilePath(boardImageDto.getUploadFilePath());
        boardImage.setUploadFileUrl(boardImageDto.getUploadFileUrl());
        boardImage.setBoard(board);
        return boardImage;
    }

    public static BoardImage of(String originalFileName, String uploadFileUrl, Board board) {
        BoardImage boardImage = new BoardImage();
        boardImage.originalFileName = originalFileName;
        boardImage.uploadFileUrl = uploadFileUrl;
        boardImage.board = board;
        return boardImage;
    }
}
