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

    /**
     * Получение событий, добавленных текущим пользователем
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     *
     * @param userId id текущего пользователя (required)
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора (optional, default to 0)
     * @param size   количество элементов в наборе (optional, default to 10)
     * @return List&lt;EventShortDto&gt;
     */
    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvent(@PathVariable Long userId,
                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                        HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
       // statisticService.addEndpointHit(request);
        log.info("GET PrivateApiController/ createEvent/, userId {}", userId);
        return eventService.getEvents(userId, from, size);
    }

    /**
     * Добавление нового события.
     * Обратите внимание: дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
     *
     * @param body   данные добавляемого события (required)
     * @param userId id текущего пользователя (required)
     * @return EventFullDto
     */

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@Valid @RequestBody NewEventDto body, @PathVariable Long userId) {
        log.info("POST PrivateApiController/createEvent/ userId {}", userId);
        return eventService.createEvent(userId, body);
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     *
     * @param userId  id текущего пользователя (required)
     * @param eventId id события (required)
     * @return EventFullDto
     */
    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventFullById(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        //statisticService.addEndpointHit(request);
        log.info("GET PrivateApiController/createEvent/ userId {}", userId);
        return eventService.getEventByUserFullById(userId, eventId);
    }


    /**
     * Изменение события добавленного текущим пользователем.
     * Обратите внимание: - изменить можно только отмененные события или события в состоянии ожидания модерации
     * (Ожидается код ошибки 409)
     * - дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
     * (Ожидается код ошибки 409)
     *
     * @param body    Новые данные события (required)
     * @param userId  id текущего пользователя (required)
     * @param eventId id редактируемого события (required)
     * @return EventFullDto
     */
    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventById(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @Valid @RequestBody UpdateEventRequest body) {

        return eventService.updateEventByIdUserRequest(userId, eventId, body);
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
     *
     * @param userId  id текущего пользователя (required)
     * @param eventId id события (required)
     * @return List&lt;ParticipationRequestDto&gt;
     */
    @GetMapping(value = "/{userId}/events/{eventId}/requests")
    List<ParticipationRequestDto> getEventParticipants(@NotNull @PathVariable("userId") Long userId,
                                                       @NotNull @PathVariable("eventId") Long eventId) {
        return requestService.getEventParticipants(userId, eventId);
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
     *
     * @param userId id текущего пользователя (required)
     * @return List&lt;ParticipationRequestDto&gt;
     *                      deserialize the response body
     */
    @GetMapping(value = "/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     * Обратите внимание: - нельзя добавить повторный запрос
     * (Ожидается код ошибки 409)
     * - инициатор события  не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
     * - нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
     * - если у события достигнут лимит запросов на участие - необходимо вернуть ошибку  (Ожидается код ошибки 409)
     * - если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти
     * в состояние подтвержденного
     *
     * @param userId  id текущего пользователя (required)
     * @param eventId id события (required)
     * @return ParticipationRequestDto
     */
    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable Long userId,
                                                           @RequestParam Long eventId,
                                                           HttpServletRequest request) {
        log.info("POST PrivateApiController/ addParticipationRequest/ userId {}", userId);
        log.info("PATH {}, queryString {}", request.getRequestURI(), request.getQueryString());
        return requestService.createRequest(userId, eventId);
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     * Обратите внимание: - если для события лимит заявок равен 0 или отключена пре-модерация заявок,
     * то подтверждение заявок не требуется - нельзя подтвердить заявку, если уже достигнут лимит
     * по заявкам на данное событие (Ожидается код ошибки 409) - статус можно изменить только у заявок,
     * находящихся в состоянии ожидания (Ожидается код ошибки 409) - если при подтверждении данной заявки,
     * лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
     *
     * @param body    Новый статус для заявок на участие в событии текущего пользователя (required)
     * @param userId  id текущего пользователя (required)
     * @param eventId id события текущего пользователя (required)
     * @return EventRequestStatusUpdateResult
     */

    @PatchMapping("{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult changeRequestStatus(@Validated @RequestBody EventRequestStatusUpdateRequest body,
                                                              @PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return requestService.changeRequestStatus(body, userId, eventId);
    }

    /**
     * Отмена своего запроса на участие в событии
     *
     * @param userId    id текущего пользователя (required)
     * @param requestId id запроса на участие (required)
     * @return ParticipationRequestDto
     */
    @PatchMapping(value = "{userId}/requests/{requestId}/cancel")
    ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                          @PathVariable("requestId") Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
