package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.entity.BoardImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardImageResponse {
    private String originalName;
    private String s3url;

    public static BoardImageResponse of(BoardImage boardImage) {
        return new BoardImageResponse(
                boardImage.getOriginalFileName(),
                boardImage.getUploadFileUrl()
        );
    }
}
