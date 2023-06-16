package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.JwtDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.request.UserLoginRequest;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.dto.response.UserLoginResponse;
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
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final AmazonS3Service amazonS3Service;
    private final DepositAccountService depositAccountService;
    private final FamilyService familyService;
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

    public UserDto join(UserJoinRequest request, MultipartFile file) {
        checkDuplicateUserInfo(request);

//        request.setPassword(encoder.encode(request.getPassword()));
        String profileUrl = setProfileUrl(file);
        User user = userRepository.save(User.of(request, profileUrl));

        depositAccountService.createDepositAccount(user);
        familyService.joinFamily(user, request);

        return UserDto.of(user);
    }

    public UserLoginResponse login(UserLoginRequest request) {
        User user = getUserOrException(request.getEmail());

        // 비번 암호화 하면 수정
//        if (!encoder.matches(request.getPassword(), user.getPassword())) {
//            throw new BaseException(BaseResponseStatus.PASSWORD_ERROR);
//        }
        if (!request.getPassword().equals(user.getPassword())) {
            throw new BaseException(INVALID_PASSWORD);
        }
        return loginResponse(user, request);
    }

    public UserLoginResponse simpleLogin(UserLoginRequest request) {
        User user = getUserOrException(request.getEmail());

        if (!request.getPassword().equals(user.getSimplePassword())) {
            throw new BaseException(INVALID_PASSWORD);
        }
        return loginResponse(user, request);
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

    public UserLoginResponse loginResponse(User user, UserLoginRequest request) {
        JwtDto tokens = createJwtDto(user, request.getEmail());

        return UserLoginResponse.builder()
                .email(request.getEmail())
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .build();
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
