package com.onehana.server_ilogu.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        PrintWriter writer = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        BaseResponseStatus status;

        if (authException instanceof InvalidHeaderException) {
            status = BaseResponseStatus.INVALID_HEADER;
        } else if (authException instanceof ExpiredTokenException) {
            status = BaseResponseStatus.EXPIRED_ACCESS_TOKEN;
        } else {
            status = BaseResponseStatus.INVALID_PERMISSION;
        }

        BaseResponse<Object> baseResponse = new BaseResponse<>(status);
        String jsonResponse = objectMapper.writeValueAsString(baseResponse);

        writer.print(jsonResponse);
        writer.flush();
    }
}
