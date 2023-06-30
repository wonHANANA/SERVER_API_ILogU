package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.EventDto;
import com.onehana.server_ilogu.dto.ProductDto;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HomeResponse {
    private BoardCategory familyType;
    private List<String> keywords;
    private List<EventDto> events;
    private List<ProductDto> products;

    public static HomeResponse of(BoardCategory familyType, List<String> keywords,
                                  List<EventDto> events, List<ProductDto> products) {
        return new HomeResponse(
                familyType,
                keywords,
                events,
                products
        );
    }
}
