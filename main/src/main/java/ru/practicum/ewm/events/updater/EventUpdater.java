package ru.practicum.ewm.events.updater;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.ewm.events.model.Event;

@Mapper
public interface EventUpdater {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(Event newEvent, @MappingTarget Event oldEvent);
}
