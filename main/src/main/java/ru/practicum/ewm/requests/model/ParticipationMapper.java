package ru.practicum.ewm.requests.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mapstruct.control.MappingControl;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.requests.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.users.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParticipationMapper {

    public static ParticipationRequest toParticipationRequest(ParticipationRequestDto requestDto,
                                                              Event event,
                                                              User user) {
        return ParticipationRequest.builder()
                .id(requestDto.getId())
                .created(requestDto.getCreated())
                .event(event)
                .requester(user)
                .status(requestDto.getStatus())
                .build();
    }

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