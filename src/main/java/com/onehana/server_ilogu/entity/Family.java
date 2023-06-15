package com.onehana.server_ilogu.entity;

import com.onehana.server_ilogu.dto.FamilyDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private List<UserFamily> members = new ArrayList<>();

    private Family(String familyName, String inviteCode) {
        this.familyName = familyName;
        this.inviteCode = inviteCode;
    }

    public static Family of(String familyName) {
        return new Family(
                familyName,
                UUID.randomUUID().toString()
        );
    }
}
