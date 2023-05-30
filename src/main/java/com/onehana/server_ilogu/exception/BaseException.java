package com.onehana.server_ilogu.exception;

import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {

    private BaseResponseStatus status;
}
