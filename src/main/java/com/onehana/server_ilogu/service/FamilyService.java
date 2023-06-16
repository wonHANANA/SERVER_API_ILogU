package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.FamilyDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.Family;
import com.onehana.server_ilogu.entity.UserFamily;
import com.onehana.server_ilogu.entity.enums.FamilyType;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.FamilyRepository;
import com.onehana.server_ilogu.repository.UserFamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.onehana.server_ilogu.dto.response.BaseResponseStatus.INVALID_FAMILY_CREATE_PERMISSION;
import static com.onehana.server_ilogu.dto.response.BaseResponseStatus.INVALID_INVITE_CODE;

@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserFamilyRepository userFamilyRepository;

    public void createFamily(User user, UserJoinRequest request) {
        Family invitedFamily = validFamilyCode(request.getInviteCode());

        if (invitedFamily != null) {
            if (request.getFamilyType().toString().equals("PARENTS")) {
                userFamilyRepository.save(UserFamily.of(user, invitedFamily, request.getFamilyType(), "부모님"));
            } else {
                userFamilyRepository.save(UserFamily.of(user, invitedFamily, request.getFamilyType(), request.getFamilyRole()));
            }
        } else {
            if (request.getFamilyType().toString().equals("PARENTS")) {
                Family newFamily = familyRepository.save(Family.of(request.getFamilyName()));
                userFamilyRepository.save(UserFamily.of(user, newFamily, request.getFamilyType(), "부모님"));
            } else {
                throw new BaseException(INVALID_FAMILY_CREATE_PERMISSION);
            }
        }
    }

    private Family validFamilyCode(String inviteCode) {
        if (inviteCode != null && !inviteCode.trim().isEmpty()) {
            return familyRepository.findByInviteCode(inviteCode).orElseThrow(() -> {
                throw new BaseException(INVALID_INVITE_CODE);
            });
        }
        return null;
    }

//    private void validateFamilyCreate(User user, String familyName) {
//        if (user.getFamilies() != null) {
//            throw new BaseException(BaseResponseStatus.EXISTING_FAMILY);
//        }
//
//        familyRepository.findByFamilyName(familyName).ifPresent(f -> {
//            throw new BaseException(BaseResponseStatus.DUPLICATED_FAMILY_NAME);
//        });
//
//        if (!user.getFamilyType().equals(FamilyType.PARENT)) {
//            throw new BaseException(BaseResponseStatus.INVALID_FAMILY_CREATE_PERMISSION);
//        }
//    }
}
