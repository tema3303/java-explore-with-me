package ru.practicum.ewm.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.error.exceptions.NotFoundException;
import ru.practicum.ewm.error.exceptions.RequestBadRequest;
import ru.practicum.ewm.error.exceptions.RequestNotPossibleCreateException;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.repository.EventRepository;
import ru.practicum.ewm.requests.enums.RequestStatus;
import ru.practicum.ewm.requests.model.ParticipationMapper;
import ru.practicum.ewm.requests.model.ParticipationRequest;
import ru.practicum.ewm.requests.model.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.requests.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.requests.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.requests.repository.RequestRepository;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    @Override
    public List<ParticipationRequestDto> getAllUserParticipationRequests(Long userId) {
        List<ParticipationRequest> requests = requestRepository.findAllByRequester_Id(userId);
        return requests.stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет данных", eventId));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет данных", userId));
        if (event.getInitiator().equals(user)) {
            throw new RequestNotPossibleCreateException("инициатор события не может добавить запрос на участие в своём событии");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new RequestNotPossibleCreateException("нельзя участвовать в неопубликованном событии");
        }
        Integer limit = event.getParticipantLimit() - event.getConfirmedRequests();
        if (event.getParticipantLimit() != 0 && limit <= 0) {
            throw new RequestNotPossibleCreateException("The participant limit has been reached");
        }
        RequestStatus status = event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED;
        if (event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        }
        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();
        if (requestRepository.existsByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new RequestNotPossibleCreateException("нельзя добавить повторный запрос");
        }
        requestRepository.save(participationRequest);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
        return ParticipationMapper.toParticipationRequestDto(participationRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelUserRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId);
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);
        return ParticipationMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByOwnerEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет данных", eventId));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет данных", userId));
        if (!event.getInitiator().equals(user)) {
            throw new RequestBadRequest("Пользователь не является организатором мероприятия");
        }
        List<ParticipationRequest> requests = requestRepository.findAllByEvent_Id(eventId);
        return requests.stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequests(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет данных", eventId));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет данных", userId));
        RequestStatus status = request.getStatus();
        List<Long> requestIds = request.getRequestIds();
        if (!event.getInitiator().equals(user)) {
            throw new RequestBadRequest("Пользователь не является организатором мероприятия");
        }
        Integer numberOfConfirmedRequests = event.getConfirmedRequests();
        Integer participantLimit = event.getParticipantLimit();
        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndIdIn(event.getId(), requestIds);
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        Integer limit = participantLimit - numberOfConfirmedRequests;
        if (event.getParticipantLimit() != 0 && limit <= 0) {
            throw new RequestNotPossibleCreateException("The participant limit has been reached");
        }
        for (ParticipationRequest r : requests) {
            if (r.getStatus() != RequestStatus.PENDING) {
                throw new RequestNotPossibleCreateException("статус можно изменить только у заявок, находящихся в состоянии ожидания");
            }
            if (status == RequestStatus.REJECTED) {
                r.setStatus(status);
                rejectedRequests.add(ParticipationMapper.toParticipationRequestDto(r));
            }
            if (numberOfConfirmedRequests < participantLimit && status == RequestStatus.CONFIRMED) {
                r.setStatus(status);
                numberOfConfirmedRequests++;
                confirmedRequests.add(ParticipationMapper.toParticipationRequestDto(r));
            } else {
                r.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(ParticipationMapper.toParticipationRequestDto(r));
            }
        }
        requestRepository.saveAll(requests);
        event.setConfirmedRequests(numberOfConfirmedRequests);
        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}