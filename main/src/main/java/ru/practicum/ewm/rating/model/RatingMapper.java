package ru.practicum.ewm.rating.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.rating.model.dto.RatingDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RatingMapper {

    public static RatingDto toRatingDto(Rating rating) {
        return RatingDto.builder()
                .id(rating.getId())
                .userId(rating.getUserId())
                .eventId(rating.getEventId())
                .isPositive(rating.getIsPositive())
                .build();
    }
}