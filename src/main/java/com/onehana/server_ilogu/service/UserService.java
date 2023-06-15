package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.JwtDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.request.UserLoginRequest;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.Family;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.FamilyRepository;
import com.onehana.server_ilogu.repository.UserRepository;
import com.onehana.server_ilogu.util.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.onehana.server_ilogu.dto.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AmazonS3Service amazonS3Service;
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.access-token.secret-key}")
    private String accessKey;

    @Value("${jwt.access-token.expired-time-ms}")
    private Long accessExpiredTime;

    @Value("${jwt.refresh-token.secret-key}")
    private String refreshKey;

    @Value("${jwt.refresh-token.expired-time-ms}")
    private Long refreshExpiredTime;

    @Transactional
    public UserDto join(UserJoinRequest request, MultipartFile file) {
        checkDuplicateUserInfo(request);

//        request.setPassword(encoder.encode(request.getPassword()));

        String profileUrl = setProfileUrl(file);
        Family family = validFamilyCode(request.getInviteCode());

        User user = userRepository.save(User.of(request, profileUrl, family));
        return UserDto.of(user);
    }

    public JwtDto login(UserLoginRequest request) {
        User user = getUserOrException(request.getEmail());

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

    private void checkDuplicateUserInfo(UserJoinRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new BaseException(DUPLICATED_EMAIL);
        }
        if(userRepository.existsByPhone(request.getPhone())) {
            throw new BaseException(DUPLICATED_PHONE);
        }
        if(userRepository.existsByNickname(request.getNickname())) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }
    }

    private String setProfileUrl(MultipartFile file) {
        if (file == null) {
            return null;
        }
        return amazonS3Service.uploadProfileImage(file);
    }

    private Family validFamilyCode(String inviteCode) {
        if (inviteCode != null && !inviteCode.trim().isEmpty()) {
            return familyRepository.findByInviteCode(inviteCode).orElseThrow(() -> {
                throw new BaseException(INVALID_INVITE_CODE);
            });
        }
        return null;
    }

    @Transactional(readOnly = true)
    public UserDto loadUserByEmail(String email) {
        return userRepository.findByEmail(email).map(UserDto::of).orElseThrow(() -> {
            throw new BaseException(USER_NOT_FOUND);
        });
    }

    public User getUserOrException(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));
    }
}
