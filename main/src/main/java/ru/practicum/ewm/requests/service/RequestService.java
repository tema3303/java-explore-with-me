package ru.practicum.ewm.requests.service;

import ru.practicum.ewm.requests.model.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.requests.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.requests.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getAllUserParticipationRequests(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelUserRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getRequestsByOwnerEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatusRequests(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest request);
}