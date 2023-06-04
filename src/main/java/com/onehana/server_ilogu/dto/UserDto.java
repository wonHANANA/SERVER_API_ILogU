package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.FamilyRole;
import com.onehana.server_ilogu.entity.FamilyType;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.entity.UserRole;
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
    private UserRole userRole;
    private FamilyType familyType;
    private FamilyRole familyRole;

    public static UserDto of(User user) {
        return new UserDto(
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
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
        return this.nickname;
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
