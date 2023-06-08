package com.onehana.server_ilogu.entity;

import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "email", unique = true)
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

    @Builder.Default
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

    public static User of(UserJoinRequest userDto, String url) {
        User user = new User();
        user.email = userDto.getEmail();
        user.password = userDto.getPassword();
        user.nickname = userDto.getNickname();
        user.familyRole = userDto.getFamilyRole();
        user.familyType = userDto.getFamilyType();
        user.profileImageUrl = url;
        return user;
    }
}
