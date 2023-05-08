package ru.practicum.mainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.Location;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.NewEventDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface EventMapper {

    @Mapping(target = "id", source = "save.id")
    EventFullDto toEventFullDto(Event save);

    EventShortDto toEventShortDto(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "state", source = "pending")
    @Mapping(target = "initiator", source = "user")
    Event toNewEvent(NewEventDto eventDto, User user, Category category, Location location, EventFullDto.StateEvent pending);

    List<EventShortDto> toShortEventList(List<Event> events);

    List<EventFullDto> toEventFullDtoList(List<Event> events);
}
