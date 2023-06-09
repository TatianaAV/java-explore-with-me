package ru.practicum.mainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.Location;
import ru.practicum.mainservice.model.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface EventMapper {

    EventFullDto toEventFullDto(Event save);

    EventShortDto toEventShortDto(EventFullDto event);

    EventShortDto toEventShortDto(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    Event toNewEvent(NewEventDto eventDto, User initiator, Category category, Location location, EventFullDto.StateEvent state);

    List<EventShortDto> toShortEventFullList(List<EventFullDto> events);

    List<EventFullDto> toEventFullDtoList(List<Event> events);
}
