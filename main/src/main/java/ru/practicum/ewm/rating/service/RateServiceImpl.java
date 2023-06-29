package ru.practicum.ewm.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.error.exceptions.NotFoundException;
import ru.practicum.ewm.error.exceptions.RequestNotPossibleCreateException;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.repository.EventRepository;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.rating.model.RatingMapper;
import ru.practicum.ewm.rating.model.dto.RatingDto;
import ru.practicum.ewm.rating.repository.RateRepository;
import ru.practicum.ewm.requests.enums.RequestStatus;
import ru.practicum.ewm.requests.repository.RequestRepository;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RateServiceImpl implements RateService {

    private final RateRepository rateRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public RatingDto addUserRateForEvent(Long userId, Long eventId, Boolean isPositive) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User", userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event", eventId));
        if (event.getInitiator().equals(user)) {
            throw new RequestNotPossibleCreateException("инициатор события не может оценить своё событие");
        }
        if (!requestRepository.existsByRequester_IdAndEvent_IdAndStatus(userId, eventId, RequestStatus.CONFIRMED)) {
            throw new RequestNotPossibleCreateException("Пользователь не посещал событие");
        }
        Rating rating;
        if (isPositive) {
            rating = Rating.builder()
                    .userId(userId)
                    .eventId(eventId)
                    .isPositive(true)
                    .build();
            event.setLikes(event.getLikes() + 1);
            event.setRate(event.getRate() + 1);
            event.getInitiator().setRate(event.getInitiator().getRate() + 1);
        } else {
            rating = Rating.builder()
                    .userId(userId)
                    .eventId(eventId)
                    .isPositive(false)
                    .build();
            event.setDislikes(event.getDislikes() + 1);
            event.setRate(event.getRate() - 1);
            event.getInitiator().setRate(event.getInitiator().getRate() - 1);
        }
        eventRepository.save(event);
        userRepository.save(user);
        return RatingMapper.toRatingDto(rateRepository.save(rating));
    }

    @Override
    @Transactional
    public void deleteUserRateForEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User", userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event", eventId));
        Rating rating = rateRepository.findByEventIdAndUserId(eventId, userId);
        if (rating == null) {
            throw new NotFoundException("Отметки не найдено");
        }
        if (rating.getIsPositive()) {
            event.setLikes(event.getLikes() - 1);
            event.setRate(event.getRate() - 1);
            event.getInitiator().setRate(event.getInitiator().getRate() - 1);
        } else {
            event.setDislikes(event.getLikes() - 1);
            event.setRate(event.getRate() + 1);
            event.getInitiator().setRate(event.getInitiator().getRate() + 1);
        }
        eventRepository.save(event);
        rateRepository.deleteByEventIdAndUserId(eventId, userId);
    }
}