package com.onehana.server_ilogu.exception;

import com.onehana.server_ilogu.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public BaseResponse<?> errorHandler(BaseException e) {
        return new BaseResponse<>(e.getStatus());
    }
}
