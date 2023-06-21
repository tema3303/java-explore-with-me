package ru.practicum.ewm.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.events.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> , QuerydslPredicateExecutor<Event>  {

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);
    List<Event> findAllByIdIn(List<Long> events);
    Event findByIdAndState(Long id, State state);

}
