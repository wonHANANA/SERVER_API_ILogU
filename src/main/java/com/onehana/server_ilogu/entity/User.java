package com.onehana.server_ilogu.entity;

import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.entity.enums.FamilyType;
import com.onehana.server_ilogu.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "email", unique = true),
})
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String simplePassword;
    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false, unique = true)
    private String phone;
    private String profileImageUrl;

    @Setter
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToOne
    @JoinColumn(name = "family_id")
    @Setter
    private Family family;

    @Enumerated(EnumType.STRING)
    @Setter
    private FamilyType familyType;
    @Setter
    private String familyRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DepositAccount> depositAccounts = new ArrayList<>();

    public void joinFamily(Family family, FamilyType familyType, String familyRole) {
        this.family = family;
        this.familyType = familyType;
        this.familyRole = familyRole;
    }

    public static User of(UserJoinRequest request, String url) {
        User user = new User();
        user.email = request.getEmail();
        user.password = request.getPassword();
        user.simplePassword = request.getSimplePassword();
        user.nickname = request.getNickname();
        user.username = request.getUsername();
        user.phone = request.getPhone();
        user.userRole = UserRole.USER_ROLE;
        user.profileImageUrl = url;
        return user;
    }
}
