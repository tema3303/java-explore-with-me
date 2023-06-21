package ru.practicum.ewm.events.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.events.enums.State;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminEventFilter {

    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    private DateRange dateRange;

}
