package ru.practicum.ewm.compilations.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.compilations.model.CompilationMapper;
import ru.practicum.ewm.compilations.model.QCompilation;
import ru.practicum.ewm.compilations.model.dto.CompilationDto;
import ru.practicum.ewm.compilations.model.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.model.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilations.repository.CompilationRepository;
import ru.practicum.ewm.error.exceptions.NotFoundException;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.EventMapper;
import ru.practicum.ewm.events.model.dto.EventShortDto;
import ru.practicum.ewm.events.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        List<Event> events = (newCompilationDto.getEvents() != null) ?
                eventRepository.findAllByIdIn(newCompilationDto.getEvents()) : null;
        compilation.setEvents(events);
        List<EventShortDto> eventShort = new ArrayList<>();
        if (events != null) {
            eventShort = events.stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), eventShort);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        checkCompilationId(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest newCompilation) {
        Compilation oldCompilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Нет данных", compId));
        List<Event> events = (newCompilation.getEvents() != null) ?
                eventRepository.findAllByIdIn(newCompilation.getEvents()) : null;
        oldCompilation.setEvents(events);
        List<EventShortDto> eventShort = new ArrayList<>();
        if (events != null) {
            eventShort = events.stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
        if (newCompilation.getTitle() != null) {
            oldCompilation.setTitle(newCompilation.getTitle());
        }
        if (newCompilation.getPinned() != null) {
            oldCompilation.setPinned(newCompilation.getPinned());
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(oldCompilation), eventShort);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pagination = PageRequest.of(from / size, size);
        BooleanExpression expression = Expressions.asBoolean(true).eq(true);
        if (pinned != null) {
            expression = expression.and(QCompilation.compilation.pinned.eq(pinned));
        }
        List<Compilation> compilations = compilationRepository.findAll(expression, pagination).getContent();
        List<Event> events = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<Event> compilationEvents = compilation.getEvents();
            for (Event event : compilationEvents) {
                events.add(event);
            }
        }
        List<EventShortDto> result = events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return compilations.stream()
                .map((Compilation compilation) -> CompilationMapper.toCompilationDto(compilation, result))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Category", compId));
        List<EventShortDto> eventShort = compilation.getEvents().stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Category", compId)), eventShort);
    }

    private void checkCompilationId(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Category", compId);
        }
    }
}