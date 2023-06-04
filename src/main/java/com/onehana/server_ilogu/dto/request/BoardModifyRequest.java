package com.onehana.server_ilogu.dto.request;

import com.onehana.server_ilogu.entity.BoardCategory;
import lombok.Data;

@Data
public class BoardModifyRequest {
    private Long id;
    private String title;
    private String content;
    private BoardCategory category;
}
