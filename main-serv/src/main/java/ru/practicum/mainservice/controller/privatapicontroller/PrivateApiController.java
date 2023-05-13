package ru.practicum.mainservice.controller.privatapicontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.ParticipationRequest.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.dto.ParticipationRequest.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.ParticipationRequest.ParticipationRequestDto;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.dto.event.UpdateEventRequest;
import ru.practicum.mainservice.service.event.EventService;
import ru.practicum.mainservice.service.partrequest.ParticipationRequestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users")
public class PrivateApiController {

    private final EventService eventService;
    private final ParticipationRequestService requestService;

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvent(@PathVariable Long userId,
                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                        HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        log.info("GET PrivateApiController/ createEvent/, userId {}", userId);
        return eventService.getEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@Valid @RequestBody NewEventDto body, @PathVariable Long userId) {
        log.info("POST PrivateApiController/createEvent/ userId {}", userId);
        return eventService.createEvent(userId, body);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventFullById(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        log.info("GET PrivateApiController/createEvent/ userId {}", userId);
        return eventService.getEventByUserFullById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventById(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @Valid @RequestBody UpdateEventRequest body) {

        return eventService.updateEventByIdUserRequest(userId, eventId, body);
    }

    @GetMapping(value = "/{userId}/events/{eventId}/requests")
    List<ParticipationRequestDto> getEventParticipants(@NotNull @PathVariable("userId") Long userId,
                                                       @NotNull @PathVariable("eventId") Long eventId) {
        return requestService.getEventParticipants(userId, eventId);
    }


    @GetMapping(value = "/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable Long userId,
                                                           @RequestParam Long eventId,
                                                           HttpServletRequest request) {
        log.info("POST PrivateApiController/ addParticipationRequest/ userId {}", userId);
        log.info("PATH {}, queryString {}", request.getRequestURI(), request.getQueryString());
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult changeRequestStatus(@Valid @RequestBody EventRequestStatusUpdateRequest body,
                                                              @PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return requestService.changeRequestStatus(body, userId, eventId);
    }

    @PatchMapping(value = "{userId}/requests/{requestId}/cancel")
    ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                          @PathVariable("requestId") Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
