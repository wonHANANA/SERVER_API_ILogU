package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.FamilyDto;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.Family;
import com.onehana.server_ilogu.entity.FamilyType;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserService userService;

    public FamilyDto createFamily(FamilyDto familyDto, String email) {
        User user = userService.getUserOrException(email);

        validateFamilyCreate(user, familyDto.getFamilyName());

        Family family = familyRepository.save(Family.toEntity(familyDto, user));
        user.setFamily(family);
        return FamilyDto.of(family);
    }

    public String joinFamily(String inviteCode, String email) {
        User user = userService.getUserOrException(email);
        Family family = familyRepository.findByInviteCode(inviteCode).orElseThrow(() ->
                new BaseException(BaseResponseStatus.INVALID_INVITE_CODE));

        if (user.getFamily() != null && user.getFamily().equals(family)) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_FAMILY);
        }
        user.setFamily(family);
        return family.getFamilyName();
    }

    public String leaveFamily(String email) {
        User user = userService.getUserOrException(email);

        Family family = user.getFamily();
        if (family == null) {
            throw new BaseException(BaseResponseStatus.NO_FAMILY_TO_LEAVE);
        }

        family.getMembers().remove(user);
        user.setFamily(null);

        if (family.getMembers().isEmpty()) {
            familyRepository.delete(family);
            return "마지막 가족이 떠났으므로, " + family.getFamilyName() + "이 해산했습니다.";
        }

        return user.getNickname() + "이 탈퇴했습니다.";
    }

    private void validateFamilyCreate(User user, String familyName) {
        if (user.getFamily() != null) {
            throw new BaseException(BaseResponseStatus.EXISTING_FAMILY);
        }

        familyRepository.findByFamilyName(familyName).ifPresent(f -> {
            throw new BaseException(BaseResponseStatus.DUPLICATED_FAMILY_NAME);
        });

        if (!user.getFamilyType().equals(FamilyType.PARENT)) {
            throw new BaseException(BaseResponseStatus.INVALID_FAMILY_CREATE_PERMISSION);
        }
    }
}
