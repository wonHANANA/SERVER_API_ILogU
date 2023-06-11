package com.onehana.server_ilogu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsResponse {
    String requestId;
    LocalDateTime requestTime;
    String statusCode;
    String statusName;
}
