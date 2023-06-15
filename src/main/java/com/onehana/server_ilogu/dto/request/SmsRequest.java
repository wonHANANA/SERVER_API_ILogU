package com.onehana.server_ilogu.dto.request;

import com.onehana.server_ilogu.dto.SmsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsRequest {
    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<SmsDto> messages;
}
