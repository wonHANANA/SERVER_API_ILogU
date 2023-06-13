package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.dto.request.UserJoinRequest;
import com.onehana.server_ilogu.entity.enums.FamilyRole;
import com.onehana.server_ilogu.entity.enums.FamilyType;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDto implements UserDetails {
    private String email;
    private String password;
    private String nickname;
    private String username;
    private String phone;
    private UserRole userRole;
    private FamilyType familyType;
    private FamilyRole familyRole;

    public static UserDto of(UserJoinRequest request) {
        return new UserDto(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getUsername(),
                request.getPhone(),
                UserRole.USER_ROLE,
                request.getFamilyType(),
                request.getFamilyRole()
        );
    }

    public static UserDto of(User user) {
        return new UserDto(
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getUsername(),
                user.getPhone(),
                user.getUserRole(),
                user.getFamilyType(),
                user.getFamilyRole()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
