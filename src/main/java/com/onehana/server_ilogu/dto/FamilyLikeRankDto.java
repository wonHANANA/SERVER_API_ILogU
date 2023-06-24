package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FamilyLikeRankDto {

    private String username;
    private String profileUrl;
    private int likes;

    public static FamilyLikeRankDto of(User user, int likes) {
        return new FamilyLikeRankDto(
                user.getUsername(),
                user.getProfileImageUrl(),
                likes
        );
    }
}
