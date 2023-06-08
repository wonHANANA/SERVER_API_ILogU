package com.onehana.server_ilogu.entity;

import com.onehana.server_ilogu.dto.FamilyDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Family extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String familyName;

    @Column(unique = true)
    private String inviteCode;

    @OneToMany(mappedBy = "family")
    private List<User> members = new ArrayList<>();

    public static Family toEntity(FamilyDto familyDto, User user) {
        Family family = new Family();
        family.familyName = familyDto.getFamilyName();
        family.inviteCode = familyDto.getInviteCode();
        family.members.add(user);
        return family;
    }
}
