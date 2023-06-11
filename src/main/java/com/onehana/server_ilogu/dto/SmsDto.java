package com.onehana.server_ilogu.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsDto {
    String to;
    String content;
}
