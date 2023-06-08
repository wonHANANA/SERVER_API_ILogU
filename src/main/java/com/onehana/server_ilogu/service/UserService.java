package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.JwtDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.request.UserLoginRequest;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.UserRepository;
import com.onehana.server_ilogu.util.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.onehana.server_ilogu.dto.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AmazonS3Service amazonS3Service;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.access-token.secret-key}")
    private String accessKey;

    @Value("${jwt.access-token.expired-time-ms}")
    private Long accessExpiredTime;

    @Value("${jwt.refresh-token.secret-key}")
    private String refreshKey;

    @Value("${jwt.refresh-token.expired-time-ms}")
    private Long refreshExpiredTime;

    @Transactional(readOnly = true)
    public UserDto loadUserByEmail(String email) {
        return userRepository.findByEmail(email).map(UserDto::of).orElseThrow(() -> {
            throw new BaseException(USER_NOT_FOUND);
        });
    }

    @Transactional
    public UserDto join(UserJoinRequest request, MultipartFile file) {
        userRepository.findByEmail(request.getEmail()).ifPresent(it -> {
            throw new BaseException(DUPLICATED_EMAIL);
        });

//        request.setPassword(encoder.encode(request.getPassword()));

        String profileUrl = "";
        if (file == null) {
            profileUrl = null;
        } else {
            profileUrl = amazonS3Service.uploadProfileImage(file);
        }
        User user = userRepository.save(User.of(request, profileUrl));
        return UserDto.of(user);
    }

    public JwtDto login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            throw new BaseException(USER_NOT_FOUND);
        });

        // 비번 암호화 하면 수정
//        if (!encoder.matches(request.getPassword(), user.getPassword())) {
//            throw new BaseException(BaseResponseStatus.PASSWORD_ERROR);
//        }
        if (!request.getPassword().equals(user.getPassword())) {
            throw new BaseException(INVALID_PASSWORD);
        }

        return createJwtDto(user, request.getEmail());
    }

    public JwtDto refresh(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken).orElseThrow(() -> {
            throw new BaseException(INVALID_REFRESH_TOKEN);
        });

        if (JwtTokenUtils.isExpired(refreshToken, refreshKey)) {
            throw new BaseException(EXPIRED_REFRESH_TOKEN);
        }

        return createJwtDto(user, user.getEmail());
    }

    private JwtDto createJwtDto(User user, String email) {
        String accessToken = JwtTokenUtils.generateAccessToken(email, accessKey, accessExpiredTime);
        String refreshToken = JwtTokenUtils.generateRefreshToken(refreshKey, refreshExpiredTime);

        JwtDto jwtDto = JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return jwtDto;
    }

    public User getUserOrException(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));
    }
}
