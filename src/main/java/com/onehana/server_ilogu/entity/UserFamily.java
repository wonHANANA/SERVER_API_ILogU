package com.onehana.server_ilogu.entity;

import com.onehana.server_ilogu.entity.enums.FamilyRole;
import com.onehana.server_ilogu.entity.enums.FamilyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserFamily extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @Enumerated(EnumType.STRING)
    private FamilyType familyType;

    @Enumerated(EnumType.STRING)
    private FamilyRole familyRole;
}
