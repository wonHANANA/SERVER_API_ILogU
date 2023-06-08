package com.onehana.server_ilogu.entity;

import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "family_id")
})
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

    private String profileImageUrl;

    @Setter
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER_ROLE;

    @Enumerated(EnumType.STRING)
    private FamilyType familyType;

    @Enumerated(EnumType.STRING)
    private FamilyRole familyRole;

    @ManyToOne
    @JoinColumn(name = "family_id")
    @Setter
    private Family family;

    public static User of(UserJoinRequest request, String url, Family family) {
        User user = new User();
        user.email = request.getEmail();
        user.password = request.getPassword();
        user.nickname = request.getNickname();
        user.familyRole = request.getFamilyRole();
        user.familyType = request.getFamilyType();
        user.profileImageUrl = url;
        user.family = family;
        return user;
    }
}
