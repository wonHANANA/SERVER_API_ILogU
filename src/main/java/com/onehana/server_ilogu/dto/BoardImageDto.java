package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.BoardImage;
import lombok.*;

@Data
@Builder
public class BoardImageDto {
    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;

    public BoardImage toEntity() {
        BoardImage boardImage = new BoardImage();
        boardImage.setOriginalFileName(this.originalFileName);
        boardImage.setUploadFileName(this.uploadFileName);
        boardImage.setUploadFilePath(this.uploadFilePath);
        boardImage.setUploadFileUrl(this.uploadFileUrl);
        return boardImage;
    }
}
