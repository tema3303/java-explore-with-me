package ru.practicum.ewm.requests.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.requests.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.users.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParticipationMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}