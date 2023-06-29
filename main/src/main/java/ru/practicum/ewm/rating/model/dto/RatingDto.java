package ru.practicum.ewm.rating.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {
    private Long id;
    private Long userId;
    private Long eventId;
    private Boolean isPositive;
}