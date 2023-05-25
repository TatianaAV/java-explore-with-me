package ru.practicum.mainservice.service.event;

import dto.EndpointDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.event.*;
import ru.practicum.mainservice.handler.NotFoundException;
import ru.practicum.mainservice.handler.NotValidatedExceptionConflict;
import ru.practicum.mainservice.handler.ValidationException;
import ru.practicum.mainservice.mapper.EventMapper;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.Location;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.*;
import ru.practicum.statsclientapp.statsclient.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.dto.event.EventFullDto.StateEvent.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {


    private final EventRepository eventRepository;
    private final EventCustomRepository eventCustomRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Transactional
    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        log.info("EventServiceImpl/users/createUser {}", eventDto);
        log.info("time event {}", eventDto.getEventDate());
        log.info("time time plus 2 hours {}", LocalDateTime.now().plusHours(2));
        Event event;
        if (eventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
            Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() -> new NotFoundException("Категория указана неверно"));
            Location locationDto = eventDto.getLocation();
            event = eventMapper.toNewEvent(eventDto, user, category, new Location(locationDto.getLat(), locationDto.getLon()), PENDING);
        } else {
            throw new ValidationException("Нельзя создать событие менее, чем за 2 часа до начала");
        }
        log.info("event {}", event.toString());
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        List<Event> events = eventRepository.findAllByInitiator_IdOrderByEventDateAsc(userId, PageRequest.of(from / size, size));
        EventStatisticsGet.addViewsAndConfirmedRequestsToEvents(eventMapper.toEventFullDtoList(events), true, statsClient, requestRepository);
        return events
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByUserFullById(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findEventByIdAndInitiator(eventId, user);
        EventStatisticsGet.addViewsAndConfirmedRequestsToEvents(List.of(eventMapper.toEventFullDto(event)), true, statsClient, requestRepository);
        return eventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEventByIdUserRequest(Long userId, Long eventId, UpdateEventRequest updateEvent) {

        Event event = eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotValidatedExceptionConflict("Событие не удовлетворяет правилам редактирования"));

        if (event.getState().equals(EventFullDto.StateEvent.PUBLISHED)) {
            throw new NotValidatedExceptionConflict("Невозможно изменить опубликованное событие, обратитесь к администратору.");
        }

        Event updated = updateEvent(event, updateEvent);
        if (updateEvent.getStateAction() == null) {
            updated.setState(PENDING);//по тестам требуется вывод эвента, при отсутствии StateAction
        } else {
            switch (updateEvent.getStateAction()) {
                case CANCEL_REVIEW:
                    updated.setState(CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    updated.setState(PENDING);
                    break;
                default:
                    break;
            }
        }
        return eventMapper.toEventFullDto(updated);
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest updateEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        Event updated = updateEvent(event, updateEvent);
        if (updateEvent.getStateAction() == null) {
            updated.setState(event.getState());//по тестам требуется вывод эвента, при отсутствии StateAction
        } else {
            switch (updateEvent.getStateAction()) {

                case PUBLISH_EVENT:

                    if (event.getState().equals(PENDING)) {

                        updated.setState(PUBLISHED);
                        updated.setPublishedOn(LocalDateTime.now());

                    } else {
                        throw new NotValidatedExceptionConflict("Событие не удовлетворяет правилам редактирования ");
                    }
                    break;

                case REJECT_EVENT:
                    if (event.getPublishedOn() == null || event.getEventDate().isBefore(event.getPublishedOn().minusHours(1))) {

                        updated.setState(CANCELED);
                    } else {
                        throw new NotValidatedExceptionConflict("Событие не удовлетворяет правилам редактирования " + event.getEventDate());
                    }
                    break;
                default:
                    break;
            }
        }
        return eventMapper.toEventFullDto(updated);
    }

    @Override
    public EventFullDto getPublicEventById(Long eventId, HttpServletRequest request) {
        log.info("uri {}", request.getRequestURI());
        EventFullDto event = eventMapper.toEventFullDto(eventRepository.findByIdAndState(eventId, PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие не найдено")));
        sendStatistics(request);
        EventStatisticsGet.addViewsAndConfirmedRequestsToEvents(List.of(event), true, statsClient, requestRepository);

        return event;
    }

    @Override
    public List<EventShortDto> getEventsUserWithFilter(EntenteParams ententeParams, HttpServletRequest request) {
        String sort = ententeParams.getSort();
        Boolean onlyAvailable = ententeParams.getOnlyAvailable();
        sendStatistics(request);

        List<Event> events = eventCustomRepository
                .findByEventUserWithFilter(ententeParams);
        List<EventFullDto> eventsFull = events.stream().map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
        EventStatisticsGet.addViewsAndConfirmedRequestsToEvents(eventsFull, true, statsClient, requestRepository);

        if (onlyAvailable != null && onlyAvailable.equals(true)) {
            eventsFull = eventsFull.stream()
                    .filter(event -> event.getParticipantLimit() <= event.getConfirmedRequests())
                    .collect(Collectors.toList());
        }


        if (Objects.equals(sort, "VIEWS")) {
            eventsFull.sort((event1, event2) -> Long.compare(event2.getViews(), event1.getViews()));
        }

        return eventsFull
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EventFullDto> getEventsByAdminWithFilter(EntenteParams ententeParams) {
        List<EventFullDto> events = eventMapper.toEventFullDtoList(eventCustomRepository.findEventByAdminWithFilter(ententeParams));
        EventStatisticsGet.addViewsAndConfirmedRequestsToEvents(events, true, statsClient, requestRepository);
        log.info("GetEventsWithFilterByAdmin/ events.get(0). {} ", events.get(0).getConfirmedRequests().toString());
        return events;
    }

    private Event updateEvent(Event event, UpdateEventRequest updateEvent) {
        if (updateEvent == null) {
            return event;
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }

        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null && !updateEvent.getCategory().equals(event.getCategory().getId())) {
            event.setCategory(
                    categoryRepository.findById(updateEvent.getCategory())
                            .orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        if (updateEvent.getEventDate() != null && !updateEvent.getEventDate().equals(event.getEventDate())) {
            if (updateEvent.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
                event.setEventDate(updateEvent.getEventDate());
            } else {
                throw new ValidationException("Дата может быть более 2 часов от текущей");
            }
        }
        if (updateEvent.getLocation() != null && !updateEvent.getLocation().equals(event.getLocation())) {
            event.setLocation(new Location(updateEvent.getLocation().getLat(), updateEvent.getLocation().getLon()));
        }
        if (updateEvent.getPaid() != null && !updateEvent.getPaid().equals(event.getPaid())) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null && !updateEvent.getParticipantLimit().equals(event.getParticipantLimit())) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null && !updateEvent.getRequestModeration().equals(event.getRequestModeration())) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().equals(event.getTitle())) {
            event.setTitle(updateEvent.getTitle());
        }
        return event;
    }

    private void sendStatistics(HttpServletRequest request) {
        String appName = "ewm";

        statsClient.addEndpointHit(new EndpointDto(
                appName,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        ));
    }
}

