package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.JwtDto;
import com.onehana.server_ilogu.dto.MyPageDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.request.UserLoginRequest;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.dto.response.UserLoginResponse;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.FamilyRepository;
import com.onehana.server_ilogu.repository.UserRepository;
import com.onehana.server_ilogu.util.CustomUserDetails;
import com.onehana.server_ilogu.util.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final FamilyRepository familyRepository;

    public UserDto join(UserJoinRequest request) {
        isDuplicatedEmail(request.getEmail());
        isDuplicatedNickname(request.getNickname());

//        request.setPassword(encoder.encode(request.getPassword()));
        User user = userRepository.save(User.of(request));

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

    public MyPageDto myPage(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new BaseException(USER_NOT_FOUND);
        });

        return MyPageDto.of(user.getNickname(), user.getProfileImageUrl());
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

    public void isDuplicatedEmail(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new BaseException(DUPLICATED_EMAIL);
        }
    }

    public void isDuplicatedNickname(String nickname) {
        if(userRepository.existsByNickname(nickname)) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }
    }

    public void isDuplicatedFamilyName(String familyName) {
        if(familyRepository.existsByFamilyName(familyName)) {
            throw new BaseException(DUPLICATED_FAMILY_NAME);
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
                .familyType(user.getFamilyType())
                .imageUrl(user.getProfileImageUrl())
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .build();
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(USER_NOT_FOUND));

        return new CustomUserDetails(user);
    }

    public User getUserOrException(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));
    }
}
