package ru.practicum.ewm.events.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DateRange {

    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;

}