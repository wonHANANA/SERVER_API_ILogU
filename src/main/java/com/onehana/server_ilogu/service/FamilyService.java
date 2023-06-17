package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.Child;
import com.onehana.server_ilogu.entity.Family;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.entity.enums.FamilyType;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.FamilyRepository;
import com.onehana.server_ilogu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserDto> getFamilyMembers(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND));

        Family family = user.getFamily();

        List<User> familyMembers = family.getMembers();

        return familyMembers.stream()
                .map(UserDto::of)
                .collect(Collectors.toList());
    }

    private void addUserToFamily(User user, Family family, UserJoinRequest request) {
        String role;
        if (request.getFamilyType() == FamilyType.PARENTS) {
            role = "부모님";
            long parentCount = family.getMembers().stream()
                    .filter(member -> member.getFamilyType() == FamilyType.PARENTS)
                    .count();
            if (parentCount >= 2) {
                throw new BaseException(ALREADY_TWO_PARENTS);
            }
        } else {
            role = request.getFamilyRole();
        }
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
        Child child = Child.of(request.getChildName(), request.getChildBirth(), BigDecimal.ZERO);
        Family newFamily = familyRepository.save(Family.of(familyName, child));
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
