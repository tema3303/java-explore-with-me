package ru.practicum.ewm.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.rating.model.Rating;

public interface RateRepository extends JpaRepository<Rating, Long> {
    Rating findByEventIdAndUserId(Long eventId, Long userId);
    void deleteByEventIdAndUserId(Long eventId, Long userId);
}