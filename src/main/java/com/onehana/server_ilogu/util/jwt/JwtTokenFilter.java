package com.onehana.server_ilogu.util.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.exception.ExpiredTokenException;
import com.onehana.server_ilogu.service.UserService;
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

        String servletPath = request.getServletPath();
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        List<String> excludePaths = Arrays.asList("/api/user/login", "/api/user/join",
                "/api/user/token/refresh", "/api/board/category");

        if (excludePaths.contains(servletPath) ||
                servletPath.equals("/api/user/board") && request.getMethod().equals("GET")){
            filterChain.doFilter(request, response);
            return;
        }

        if (header == null || !header.startsWith("Bearer ")) {
            log.error("Header가 null이거나 잘못된 형식입니다.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.split(" ")[1].trim();

            if (JwtTokenUtils.isExpired(token, key)) {
                throw new ExpiredTokenException(BaseResponseStatus.EXPIRED_ACCESS_TOKEN.getMessage());
            }

            String email = JwtTokenUtils.getEmail(token, key);
            UserDto user = userService.loadUserByEmail(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredTokenException e) {
            log.error("토큰이 만료되었습니다.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            writeErrorResponse(response, BaseResponseStatus.EXPIRED_ACCESS_TOKEN);
            return;
        } catch (RuntimeException e) {
            log.error("토큰이 유효하지 않습니다.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            writeErrorResponse(response, BaseResponseStatus.INVALID_ACCESS_TOKEN);
            return;
        }

        filterChain.doFilter(request, response);
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
