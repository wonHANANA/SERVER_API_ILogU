package com.onehana.server_ilogu.exception;

import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(HttpMessageNotReadableException.class)    // 잘못된 json 정보 처리
    public BaseResponse<Object> HttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info("HttpMessageNotReadable " + e.getMessage());
        BaseResponseStatus status = BaseResponseStatus.INVALID_JSON_REQUEST;
        return new BaseResponse<>(status);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)    // 잘못된 http 메소드 처리
    public BaseResponse<Object> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("HttpRequestMethodNotSupportedException " + e.getMessage());
        BaseResponseStatus status = BaseResponseStatus.INVALID_HTTP_REQUEST;
        return new BaseResponse<>(status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)    // 유효성 검사 실패
    public BaseResponse<Object> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.info("MethodNotValid " + e.getMessage());
        BaseResponseStatus status = BaseResponseStatus.INVALID_JSON_REQUEST;
        return new BaseResponse<>(status, errorMessage);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)    // 잘못된 자료형을 처리
    public BaseResponse<Object> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("MethodTypeMismatch " + e.getMessage());
        BaseResponseStatus status = BaseResponseStatus.INVALID_VALUE_TYPE;
        return new BaseResponse<>(status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)    // DB 중복오류
    public BaseResponse<Object> DataIntegrityViolationException(DataIntegrityViolationException e) {
        log.info("DataIntegrityViolation " + e.getMessage());
        BaseResponseStatus status = BaseResponseStatus.DATABASE_DUPLICATE_VALUE;
        return new BaseResponse<>(status);
    }

    @ExceptionHandler(MultipartException.class)
    public BaseResponse<Object> MultipartException(MultipartException e) {
        log.info("MultipartException " + e.getMessage());
        BaseResponseStatus status = BaseResponseStatus.MULTI_PART_EXCEPTION;
        return new BaseResponse<>(status);
    }

    @ExceptionHandler(BaseException.class)
    public BaseResponse<?> errorHandler(BaseException e) {
        log.info("BaseException 에서 처리");
        return new BaseResponse<>(e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> Exception(Exception e) {
        String errorMessage = e.getMessage();
        BaseResponseStatus status = BaseResponseStatus.UNKNOWN_SERVER_ERROR;
        return new BaseResponse<>(status, errorMessage);
    }
}
