package ru.practicum.ewm.compilations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.events.model.Event;

public interface CompilationRepository extends JpaRepository <Compilation, Long>, QuerydslPredicateExecutor<Compilation> {

    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
