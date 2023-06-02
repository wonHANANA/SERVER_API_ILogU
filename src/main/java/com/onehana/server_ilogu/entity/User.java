package com.onehana.server_ilogu.entity;

import com.onehana.server_ilogu.dto.request.UserJoinRequest;
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
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private FamilyType type;

    @Enumerated(EnumType.STRING)
    private FamilyRole role;

    public static User of(UserJoinRequest userDto) {
        User user = new User();
        user.email = userDto.getEmail();
        user.password = userDto.getPassword();
        user.nickname = userDto.getNickname();
        return user;
    }
}
