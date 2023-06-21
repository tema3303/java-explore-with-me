package ru.practicum.ewm.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.model.dto.EventFullDto;
import ru.practicum.ewm.events.model.dto.EventNewDto;
import ru.practicum.ewm.events.model.dto.EventShortDto;
import ru.practicum.ewm.events.model.dto.EventUpdateUserDto;
import ru.practicum.ewm.events.service.EventService;
import ru.practicum.ewm.requests.model.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.requests.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.requests.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.requests.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable Long userId, @Valid @RequestBody EventNewDto eventNewDto) {
        log.info("Добавлено событие {}", eventNewDto);
        return eventService.addNewEvent(userId, eventNewDto);
    }

    @GetMapping
    public List<EventShortDto> getEventByOwnerShort(@PathVariable Long userId,
                                                    @RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        //log.info("Добавлено событие {}", eventNewDto);
        return eventService.getEventByOwnerShort(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByOwnerFull(@PathVariable Long userId, @PathVariable Long eventId) {
        //log.info("Добавлено событие {}", eventNewDto);
        return eventService.getEventByOwnerFull(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByOwner(@PathVariable Long userId, @PathVariable Long eventId,
                                           @Valid @RequestBody EventUpdateUserDto eventUpdateUserDto) {
        log.info("Обновлено событие {}", eventUpdateUserDto);
        return eventService.updateEventByOwner(userId, eventId, eventUpdateUserDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByOwnerEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        //log.info("Добавлено событие {}", eventNewDto);
        return requestService.getRequestsByOwnerEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest request) {
        //log.info("Обновлено событие {}", eventUpdateUserDto);
        return requestService.updateStatusRequests(userId, eventId, request);
    }
}