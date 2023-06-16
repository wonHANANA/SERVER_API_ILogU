package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.entity.Family;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.FamilyRepository;
import com.onehana.server_ilogu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.onehana.server_ilogu.dto.response.BaseResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    public void joinFamily(User user, UserJoinRequest request) {
        Family invitedFamily = validFamilyCode(request.getInviteCode());
        if (invitedFamily != null) {
            addUserToFamily(user, invitedFamily, request);
        } else if ("PARENTS".equals(request.getFamilyType().toString())) {
            createNewFamily(user, request);
        } else {
            throw new BaseException(INVALID_FAMILY_CREATE_PERMISSION);
        }
    }

    private void addUserToFamily(User user, Family family, UserJoinRequest request) {
        String role = "PARENTS".equals(request.getFamilyType().toString()) ? "부모님" : request.getFamilyRole();
        user.joinFamily(family, request.getFamilyType(), role);
        userRepository.save(user);
    }

    private void createNewFamily(User user, UserJoinRequest request) {
        String familyName = request.getFamilyName();
        if (familyName == null || familyName.trim().isEmpty()) {
            throw new BaseException(INVALID_FAMILY_NAME);
        }
        if (familyRepository.findByFamilyName(familyName).isPresent()) {
            throw new BaseException(DUPLICATED_FAMILY_NAME);
        }

        Family newFamily = familyRepository.save(Family.of(familyName));
        addUserToFamily(user, newFamily, request);
    }

    private Family validFamilyCode(String inviteCode) {
        if (inviteCode != null && !inviteCode.trim().isEmpty()) {
            return familyRepository.findByInviteCode(inviteCode).orElseThrow(() -> {
                throw new BaseException(INVALID_INVITE_CODE);
            });
        }
        return null;
    }
}
