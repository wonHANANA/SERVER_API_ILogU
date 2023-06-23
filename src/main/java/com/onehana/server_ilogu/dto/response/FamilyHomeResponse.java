package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.BoardListDto;
import com.onehana.server_ilogu.dto.FamilyLikeRankDto;
import com.onehana.server_ilogu.dto.FamilyMoneyRankDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class FamilyHomeResponse {

    List<FamilyMoneyRankDto> familyMoneyRank;
    List<FamilyLikeRankDto> familyLikeRank;
    Page<BoardListDto> familyBoards;

    public static FamilyHomeResponse of(List<FamilyMoneyRankDto> familyMoneyRanks, List<FamilyLikeRankDto> familyLikeRanks,
                                        Page<BoardListDto> familyBoards) {
        return new FamilyHomeResponse(
                familyMoneyRanks,
                familyLikeRanks,
                familyBoards
        );
    }
}
