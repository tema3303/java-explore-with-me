package ru.practicum.ewm.rating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.rating.model.dto.RatingDto;
import ru.practicum.ewm.rating.service.RateService;

@RestController
@RequestMapping("/users/{userId}/likes/events/{eventId}")
@RequiredArgsConstructor
@Slf4j
public class RatePrivateController {

    private final RateService rateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RatingDto addUserRateForEvent(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @RequestParam(defaultValue = "true") Boolean isPositive) {

        return rateService.addUserRateForEvent(userId, eventId, isPositive);
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUserRateForEvent(@PathVariable Long userId,
                                       @PathVariable Long eventId) {
        rateService.deleteUserRateForEvent(userId, eventId);
    }
}