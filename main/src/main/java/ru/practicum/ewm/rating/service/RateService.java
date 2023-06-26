package ru.practicum.ewm.rating.service;

import ru.practicum.ewm.rating.model.dto.RatingDto;

public interface RateService {

    RatingDto addUserRateForEvent(Long userId, Long eventId, Boolean isPositive);
    void deleteUserRateForEvent(Long userId, Long eventId);

}