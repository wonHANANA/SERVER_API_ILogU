package com.onehana.server_ilogu.dto;

import lombok.*;

@Data
@Builder
public class S3FileDto {
    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;
}
