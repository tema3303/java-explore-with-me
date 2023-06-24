package ru.practicum.ewm.events.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.model.CategoryMapper;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.categories.service.CategoryService;
import ru.practicum.ewm.error.exceptions.EventBadTimeException;
import ru.practicum.ewm.error.exceptions.EventNotPossibleChange;
import ru.practicum.ewm.error.exceptions.NotFoundException;
import ru.practicum.ewm.error.exceptions.ValidationException;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.events.enums.StateActionAdmin;
import ru.practicum.ewm.events.enums.StateActionUser;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.EventMapper;
import ru.practicum.ewm.events.model.QEvent;
import ru.practicum.ewm.events.model.dto.*;
import ru.practicum.ewm.events.repository.EventRepository;
import ru.practicum.ewm.stats.client.HitClient;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final HitClient hitClient;

    @Override
    public List<EventShortDto> getEventByOwnerShort(Long usersId, Integer from, Integer size) {
        List<Event> events;
        if (Objects.isNull(from) || Objects.isNull(size)) {
            throw new ValidationException("Значения не могут null");
        }
        Pageable pagination = PageRequest.of(from / size, size);
        events = eventRepository.findAllByInitiatorId(usersId, pagination);
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto addNewEvent(Long usersId, EventNewDto eventNewDto) {
        User user = userRepository.findById(usersId).orElseThrow(() -> new NotFoundException("Нет данных", usersId));
        Category category = categoryRepository.findById(eventNewDto.getCategory()).orElseThrow(()
                -> new NotFoundException("Нет данных", eventNewDto.getCategory()));
        if (eventNewDto.getEventDate() != null && !eventNewDto.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new EventBadTimeException("Only pending or canceled events can be changed");
        }
        Event event = EventMapper.toEvent(eventNewDto);
        event.setInitiator(user);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);
        event.setConfirmedRequests(0);
        event.setViews(0L);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventByOwnerFull(Long usersId, Long eventId) {
        return EventMapper.toEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, usersId));
    }

    @Override
    @Transactional
    public EventFullDto updateEventByOwner(Long usersId, Long eventId, EventUpdateUserDto newEvent) {
        StateActionUser stateActionUser = StateActionUser.fromName(newEvent.getStateAction());
        Long categoryId = newEvent.getCategory();
        Category category = categoryId != null ? CategoryMapper.toCategory(categoryService.getCategoryById(newEvent.getCategory())) : null;
        Event oldEvent = eventRepository.findByIdAndInitiatorId(eventId, usersId);
        if (oldEvent.getState() == State.PUBLISHED) {
            throw new EventNotPossibleChange("Only pending or canceled events can be changed");
        }
        if (newEvent.getAnnotation() != null) {
            oldEvent.setAnnotation(newEvent.getAnnotation());
        }
        if (newEvent.getCategory() != null) {
            oldEvent.setCategory(category);
        }
        if (newEvent.getDescription() != null) {
            oldEvent.setDescription(newEvent.getDescription());
        }
        if (newEvent.getEventDate() != null) {
            oldEvent.setEventDate(newEvent.getEventDate());
            if (!newEvent.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
                throw new EventBadTimeException("Only pending or canceled events can be changed");
            }
        }
        if (newEvent.getLocation() != null) {
            oldEvent.setLocationLat(newEvent.getLocation().getLat());
            oldEvent.setLocationLon(newEvent.getLocation().getLon());
        }
        if (newEvent.getPaid() != null) {
            oldEvent.setPaid(newEvent.getPaid());
        }
        if (newEvent.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(newEvent.getParticipantLimit());
        }
        if (newEvent.getRequestModeration() != null) {
            oldEvent.setRequestModeration(newEvent.getRequestModeration());
        }
        if (newEvent.getTitle() != null) {
            oldEvent.setTitle(newEvent.getTitle());
        }
        changeEventStateIfNecessary(oldEvent, stateActionUser);//меняем статус
        return EventMapper.toEventFullDto(eventRepository.save(oldEvent));
    }

    @Override
    public List<EventFullDto> getSearchEvents(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd, Integer from, Integer size) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Start date must be before End");
        }
        if (Objects.isNull(from) || Objects.isNull(size)) {
            throw new ValidationException("Значения не могут null");
        }
        Pageable pagination = PageRequest.of(from / size, size);
        BooleanExpression expression = Expressions.asBoolean(true).eq(true);
        if (users != null) {
            expression = expression.and(QEvent.event.initiator.id.in(users));
        }
        if (states != null) {
            expression = expression.and(QEvent.event.state.stringValue().in(states));
        }

        if (categories != null) {
            expression = expression.and(QEvent.event.category.id.in(categories));
        }

        if (rangeStart != null) {
            expression = expression.and(QEvent.event.eventDate.after(rangeStart));
        }

        if (rangeEnd != null) {
            expression = expression.and(QEvent.event.eventDate.before(rangeEnd));
        }
        List<Event> result = eventRepository.findAll(expression, pagination).getContent();
        return result.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, EventUpdateAdminDto newEvent) {
        StateActionAdmin stateActionAdmin = StateActionAdmin.fromName(newEvent.getStateAction());
        Long categoryId = newEvent.getCategory();
        Category category = categoryId != null ? CategoryMapper.toCategory(categoryService.getCategoryById(newEvent.getCategory())) : null;
        Event oldEvent = eventRepository.findById(eventId).orElseThrow();
        if (!oldEvent.getState().equals(State.PENDING) && stateActionAdmin.equals(StateActionAdmin.PUBLISH_EVENT)) {
            throw new EventNotPossibleChange("Cannot publish the event because it's not in the right state: PUBLISHED");
        }
        if (oldEvent.getState().equals(State.PUBLISHED) && stateActionAdmin.equals(StateActionAdmin.REJECT_EVENT)) {
            throw new EventNotPossibleChange("Cannot publish the event because it's not in the right state: PUBLISHED");
        }
        if (newEvent.getAnnotation() != null) {
            oldEvent.setAnnotation(newEvent.getAnnotation());
        }
        if (newEvent.getCategory() != null) {
            oldEvent.setCategory(category);
        }
        if (newEvent.getDescription() != null) {
            oldEvent.setDescription(newEvent.getDescription());
        }
        if (newEvent.getEventDate() != null) {
            oldEvent.setEventDate(newEvent.getEventDate());
            if (!newEvent.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
                throw new EventBadTimeException("Only pending or canceled events can be changed");
            }
        }
        if (newEvent.getLocation() != null) {
            oldEvent.setLocationLat(newEvent.getLocation().getLat());
            oldEvent.setLocationLon(newEvent.getLocation().getLon());
        }
        if (newEvent.getPaid() != null) {
            oldEvent.setPaid(newEvent.getPaid());
        }
        if (newEvent.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(newEvent.getParticipantLimit());
        }
        if (newEvent.getRequestModeration() != null) {
            oldEvent.setRequestModeration(newEvent.getRequestModeration());
        }
        if (newEvent.getTitle() != null) {
            oldEvent.setTitle(newEvent.getTitle());
        }
        changeEventStateIfNecessary(oldEvent, stateActionAdmin);//меняем статус
        return EventMapper.toEventFullDto(eventRepository.save(oldEvent));
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                         String sort, Integer from, Integer size, HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Start date must be before End");
        }
        if (Objects.isNull(from) || Objects.isNull(size)) {
            throw new ValidationException("Значения не могут null");
        }
        Pageable pagination = PageRequest.of(from / size, size);
        BooleanExpression expression = Expressions.asBoolean(true).eq(true);
        if (text != null) {
            expression = expression.and(QEvent.event.annotation.containsIgnoreCase(text))
                    .or(QEvent.event.description.containsIgnoreCase(text));
        }
        if (categories != null) {
            expression = expression.and(QEvent.event.category.id.in(categories));
        }
        expression = expression.and(QEvent.event.eventDate.after(Objects.requireNonNullElseGet(rangeStart, LocalDateTime::now)));
        if (rangeEnd != null) {
            expression = expression.and(QEvent.event.eventDate.before(rangeEnd));
        }
        if (onlyAvailable) {
            expression = expression.and(QEvent.event.participantLimit.goe(0));
        }
        expression = expression.and(QEvent.event.state.eq(State.PUBLISHED));
        List<Event> resultEvents = eventRepository.findAll(expression, pagination).getContent();
        setViewsForEvents(resultEvents);
        return resultEvents.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Нет данных", id));
        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("Нет данных", id);
        }
        event.setViews(event.getViews() + 1);
        return EventMapper.toEventFullDto(event);
    }

    private void changeEventStateIfNecessary(Event event, StateActionUser userActionState) {
        if (userActionState != null) {
            changeEventState(event, userActionState);
        }
    }

    private void changeEventStateIfNecessary(Event event, StateActionAdmin userActionState) {
        if (userActionState != null) {
            changeEventState(event, userActionState);
        }
    }

    private void changeEventState(Event event, StateActionUser userActionState) {
        if (userActionState == StateActionUser.CANCEL_REVIEW) {
            event.setState(State.CANCELED);
        } else if (userActionState == StateActionUser.SEND_TO_REVIEW) {
            event.setState(State.PENDING);
        }
    }

    private void changeEventState(Event event, StateActionAdmin userActionState) {
        if (userActionState == StateActionAdmin.PUBLISH_EVENT) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else if (userActionState == StateActionAdmin.REJECT_EVENT) {
            event.setState(State.CANCELED);
        }
    }

    private void setViewsForEvents(List<Event> events) {
        List<StatsDto> viewStatsList = getAllViewStatsByEventIdIn(events.stream().map(Event::getId));
        Map<Long, StatsDto> viewStatsMap = new HashMap<>();
        for (StatsDto statsDto : viewStatsList) {
            viewStatsMap.put(getEventIdFromViewStats(statsDto), statsDto);
        }
        for (Event event : events) {
            StatsDto currentViewStats = viewStatsMap.get(event.getId());
            Long views = (currentViewStats != null) ? currentViewStats.getHits() : 0;
            event.setViews(views);
        }
    }

    private List<StatsDto> getAllViewStatsByEventIdIn(Stream<Long> eventIds) {
        List<String> uris = eventIds
                .map((eventId) -> String.format("/events/%s", eventId))
                .collect(Collectors.toList());
        return hitClient.getStats("2000-01-01 00:00:00", "2100-01-01 00:00:00", uris, false);
    }

    private Long getEventIdFromViewStats(StatsDto statsDto) {
        String uri = statsDto.getUri();
        return Long.parseLong(uri.substring("/events/".length()));
    }
}