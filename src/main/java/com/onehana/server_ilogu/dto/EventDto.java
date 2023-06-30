package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventDto {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;

    public static EventDto of(Event event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getImageUrl()
        );
    }
}
