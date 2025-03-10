package com.onehana.server_ilogu.util.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.exception.ExpiredTokenException;
import com.onehana.server_ilogu.exception.InvalidHeaderException;
import com.onehana.server_ilogu.service.UserService;
import com.onehana.server_ilogu.util.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Headers", "*");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String servletPath = request.getServletPath();
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        List<String> excludePaths = Arrays.asList("/", "/api/user/login", "/api/user/simpleLogin",
                "/api/user/sendCode", "/api/user/token/refresh", "/api/board/image/explain");

        if (servletPath.startsWith("/api/user/join") || excludePaths.contains(servletPath) ||
                servletPath.contains("swagger-ui") || servletPath.contains("api-docs") || servletPath.contains("webjars")){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            validateHeader(header);
            String token = extractToken(header);

            if (JwtTokenUtils.isExpired(token, key)) {
                throw new ExpiredTokenException(BaseResponseStatus.EXPIRED_ACCESS_TOKEN.getMessage());
            }

            String email = JwtTokenUtils.getEmail(token, key);
            CustomUserDetails customUserDetails = (CustomUserDetails) userService.loadUserByEmail(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (InvalidHeaderException e) {
            handleException(response, HttpStatus.UNAUTHORIZED, BaseResponseStatus.INVALID_HEADER);
            return;
        } catch (ExpiredTokenException e) {
            handleException(response, HttpStatus.UNAUTHORIZED, BaseResponseStatus.EXPIRED_ACCESS_TOKEN);
            return;
        } catch (RuntimeException e) {
            handleException(response, HttpStatus.UNAUTHORIZED, BaseResponseStatus.INVALID_ACCESS_TOKEN);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void validateHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new InvalidHeaderException(BaseResponseStatus.INVALID_HEADER.getMessage());
        }
    }

    private String extractToken(String header) {
        return header.split(" ")[1].trim();
    }

    private void handleException(HttpServletResponse response, HttpStatus status, BaseResponseStatus baseResponseStatus) throws IOException {
        log.error(baseResponseStatus.getMessage());
        response.setStatus(status.value());
        writeErrorResponse(response, baseResponseStatus);
    }

    private void writeErrorResponse(HttpServletResponse response, BaseResponseStatus status) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter writer = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        BaseResponse<Object> baseResponse = new BaseResponse<>(status);
        String jsonResponse = objectMapper.writeValueAsString(baseResponse);

        writer.print(jsonResponse);
        writer.flush();
    }
}
