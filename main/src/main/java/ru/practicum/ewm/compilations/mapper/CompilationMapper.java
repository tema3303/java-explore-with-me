package ru.practicum.ewm.compilations.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.compilations.model.dto.CompilationDto;
import ru.practicum.ewm.compilations.model.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.model.dto.UpdateCompilationRequest;
import ru.practicum.ewm.events.model.EventMapper;

import java.util.List;

@Mapper
public interface CompilationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(UpdateCompilationRequest updateCompilationRequest);

    CompilationDto toCompilationDto(Compilation compilation);

    List<CompilationDto> toCompilationDtoList(List<Compilation> compilations);

}
