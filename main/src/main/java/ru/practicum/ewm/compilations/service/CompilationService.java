package ru.practicum.ewm.compilations.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.compilations.model.dto.CompilationDto;
import ru.practicum.ewm.compilations.model.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.model.dto.UpdateCompilationRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation (NewCompilationDto newCompilationDto);
    void deleteCompilation(Long compId);
    CompilationDto updateCompilation(Long compId,UpdateCompilationRequest updateCompilationRequest);
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
    CompilationDto getCompilationById(Long compId);
}