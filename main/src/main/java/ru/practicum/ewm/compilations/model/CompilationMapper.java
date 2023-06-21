package ru.practicum.ewm.compilations.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.compilations.model.dto.CompilationDto;
import ru.practicum.ewm.compilations.model.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.model.dto.UpdateCompilationRequest;
import ru.practicum.ewm.events.model.dto.EventShortDto;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto){
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static Compilation toCompilation(CompilationDto compilationDto){
        return Compilation.builder()
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public static Compilation toCompilation(UpdateCompilationRequest compilationDto){
        return Compilation.builder()
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> eventsShort){
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(eventsShort)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }


}
