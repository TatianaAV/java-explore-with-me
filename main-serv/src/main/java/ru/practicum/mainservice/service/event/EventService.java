package ru.practicum.mainservice.service.event;

import ru.practicum.mainservice.dto.event.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto eventDto);

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    EventFullDto getEventByUserFullById(Long userId, Long eventId);

    EventFullDto updateEventByIdUserRequest(Long userId, Long eventId, UpdateEventRequest statusEnum);

    EventFullDto updateEventByAdmin(Long compId, UpdateEventRequest updateEventRequest);

    EventFullDto getPublicEventById(Long eventId, HttpServletRequest request);

    List<EventShortDto> getEventsUserWithFilter(EntenteParams ententeParams, HttpServletRequest request);

    List<EventFullDto> getEventsByAdminWithFilter(EntenteParams ententeParams);
}
