package ru.practicum.ewm.events.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.events.model.dto.EventFullDto;
import ru.practicum.ewm.events.model.dto.EventNewDto;
import ru.practicum.ewm.events.model.dto.EventShortDto;
import ru.practicum.ewm.events.model.dto.EventUpdateUserDto;
import ru.practicum.ewm.locations.LocationDto;
import ru.practicum.ewm.users.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {


    public static Event toEvent(EventNewDto eventNewDto) {
        return Event.builder()
                .id(eventNewDto.getId())
                .annotation(eventNewDto.getAnnotation())
                .description(eventNewDto.getDescription())
                .eventDate(eventNewDto.getEventDate())
                .locationLat(eventNewDto.getLocation().getLat())
                .locationLon(eventNewDto.getLocation().getLon())
                .paid(eventNewDto.getPaid())
                .participantLimit(eventNewDto.getParticipantLimit())
                .requestModeration(eventNewDto.getRequestModeration())
                .title(eventNewDto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(event.getInitiator())
                .location(new LocationDto(event.getLocationLat(), event.getLocationLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event){
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(event.getInitiator())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(EventUpdateUserDto eventUpdateUserDto){
        return Event.builder()
                .annotation(eventUpdateUserDto.getAnnotation())
                .description(eventUpdateUserDto.getDescription())
                .eventDate(eventUpdateUserDto.getEventDate())
                .locationLat(eventUpdateUserDto.getLocation().getLat())
                .locationLon(eventUpdateUserDto.getLocation().getLon())
                .paid(eventUpdateUserDto.getPaid())
                .participantLimit(eventUpdateUserDto.getParticipantLimit())
                .requestModeration(eventUpdateUserDto.getRequestModeration())
                .title(eventUpdateUserDto.getTitle())
                .build();
    }
}
