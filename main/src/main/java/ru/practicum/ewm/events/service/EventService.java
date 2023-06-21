package ru.practicum.ewm.events.service;

import ru.practicum.ewm.events.model.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getEventByOwnerShort(Long usersId, Integer from, Integer size);

    EventFullDto addNewEvent(Long usersId, EventNewDto eventNewDto);

    EventFullDto getEventByOwnerFull(Long usersId, Long eventId);

    EventFullDto updateEventByOwner(Long usersId, Long eventId, EventUpdateUserDto eventUpdateUserDto);

    List<EventFullDto> getSearchEvents(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, EventUpdateAdminDto eventUpdateUserDto);

    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                  Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEventById(Long id,HttpServletRequest request);
}