package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FamilyLikeRankDto {

    private String nickname;
    private String profileUrl;
    private int likes;

    public static FamilyLikeRankDto of(User user, int likes) {
        return new FamilyLikeRankDto(
                user.getNickname(),
                user.getProfileImageUrl(),
                likes
        );
    }
}
