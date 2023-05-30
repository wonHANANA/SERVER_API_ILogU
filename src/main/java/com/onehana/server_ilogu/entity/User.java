package com.onehana.server_ilogu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;

    @Setter
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER_ROLE;

    @Enumerated(EnumType.STRING)
    private FamilyType type = FamilyType.PARENT;

    @Enumerated(EnumType.STRING)
    private FamilyRole role = FamilyRole.FATHER;
}
