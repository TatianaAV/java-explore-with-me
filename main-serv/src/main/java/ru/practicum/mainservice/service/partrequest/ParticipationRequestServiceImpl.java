package ru.practicum.mainservice.service.partrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.ParticipationRequest.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.dto.ParticipationRequest.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.ParticipationRequest.ParticipationRequestDto;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.handler.NotFoundException;
import ru.practicum.mainservice.handler.NotValidatedExceptionConflict;
import ru.practicum.mainservice.mapper.RequestMapper;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.ParticipationRequest;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.ParticipationRequestRepository;
import ru.practicum.mainservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final RequestMapper requestMapper;

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Войдите или зарегистрируйтесь"));
        ParticipationRequest requestVerified = validationRequest(new ParticipationRequest(null, LocalDateTime.now(), event, requester, null), event);

        ParticipationRequest requestSaved = participationRequestRepository.save(requestVerified);
        return requestMapper.toRequestDto(requestSaved);
    }

    private ParticipationRequest validationRequest(ParticipationRequest request, Event event) {
        /*нельзя добавить повторный запрос (Ожидается код ошибки 409) error sql unique*/
//инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
        if (request.getRequester().equals(request.getEvent().getInitiator())) {
            throw new NotValidatedExceptionConflict("Вы не можете подтвердить участие в своем событии");
        }
//нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
        if (!request.getEvent().getState().equals(EventFullDto.StateEvent.PUBLISHED)) {
            throw new NotValidatedExceptionConflict("Событие еще не доступно");
        }
//если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
        if (event.getParticipantLimit() != 0
                && (request.getEvent().getParticipantLimit().intValue() == participationRequestRepository
                .findByEvent_IdCount(request.getEvent().getId()))) {
            request.setStatus(ParticipationRequestDto.StatusRequest.REJECTED);
            throw new NotValidatedExceptionConflict("Достигнут лимит запросов");
        }
//если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
        if (event.getParticipantLimit() == 0 || request.getEvent().getRequestModeration().equals(false)) { //разногласие тестов и ТЗ при исправлении тестов => true премодерация, но лимит 0
            request.setStatus(ParticipationRequestDto.StatusRequest.CONFIRMED);
            return request;
        }
        request.setStatus(ParticipationRequestDto.StatusRequest.PENDING);
        return request;
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        List<ParticipationRequest> listRequest = participationRequestRepository.findByEvent_IdAndEvent_Initiator_Id(eventId, userId);
        return requestMapper.toListRequestDto(listRequest);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        /*  "При создании у запроса на участие должен быть статус PENDING, а при удалении - CANCELED"*/
        ParticipationRequest requestUser = participationRequestRepository.findByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id= " + requestId + " was not found"));
        requestUser.setStatus(ParticipationRequestDto.StatusRequest.CANCELED);

        return requestMapper.toRequestDto(requestUser);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(EventRequestStatusUpdateRequest body, Long userId, Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        Integer eventParticipants = participationRequestRepository.findByEvent_IdCount(eventId);
        Map<Long, ParticipationRequest> eventRequest = participationRequestRepository.findParticipationRequestByEvent_Initiator_IdAndEvent_Id(userId, eventId)
                .stream().collect(Collectors.toMap(ParticipationRequest::getId, request -> request));

        return mapToEventRequestStatusUpdateResult(eventRequest, eventParticipants, body, event);
    }

    private EventRequestStatusUpdateResult mapToEventRequestStatusUpdateResult(
            Map<Long, ParticipationRequest> eventRequests,
            Integer eventParticipants, EventRequestStatusUpdateRequest body, Event event) {


        Integer participantLimit = event.getParticipantLimit();
        Boolean requestModeration = event.getRequestModeration();


        List<Long> requestIds = body.getRequestIds();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult =
                new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);

        for (Long requestId : requestIds) {
            var request = eventRequests.get(requestId);
            /*  статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)*/
            if (request.getStatus().equals(ParticipationRequestDto.StatusRequest.PENDING)) {

                switch (body.getStatus()) {

                    case CONFIRMED:
                        /*Ограничение на количество участников. Значение 0 - означает отсутствие ограничения*/
                        /*отключена пре-модерация заявок, то подтверждение заявок не требуется*/
                        if (participantLimit == 0 || requestModeration.equals(false)) {
                            request.setStatus(ParticipationRequestDto.StatusRequest.CONFIRMED);
                            eventParticipants++;
                            confirmedRequests.add(requestMapper.toRequestDto(request));
                            eventParticipants++;
                            eventRequests.remove(requestId);

                        } else if (participantLimit >= 1 && participantLimit > eventParticipants) {
                            /* нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)*/
                            request.setStatus(ParticipationRequestDto.StatusRequest.CONFIRMED);
                            confirmedRequests.add(requestMapper.toRequestDto(request));
                            eventParticipants++;
                            eventRequests.remove(requestId);

                        } else {
                            /* если при подтверждении данной заявки, лимит заявок для события исчерпан,
                             то все неподтверждённые заявки необходимо отклонить*/
                            if (!eventRequests.isEmpty()) {
                                Set<Map.Entry<Long, ParticipationRequest>> set = eventRequests.entrySet();
                                for (Map.Entry<Long, ParticipationRequest> me : set) {
                                    var requestForRejectedRequests = me.getValue();
                                    if (requestForRejectedRequests.getStatus().equals(ParticipationRequestDto.StatusRequest.PENDING)) {
                                        requestForRejectedRequests.setStatus(ParticipationRequestDto.StatusRequest.REJECTED);
                                    }
                                }
                            }
                            throw new NotValidatedExceptionConflict("Достигнут лимит мест на событие");
                        }
                        break;


                    case REJECTED:
                        if (participantLimit == 0 || requestModeration.equals(false)) {
                            request.setStatus(ParticipationRequestDto.StatusRequest.REJECTED);
                            rejectedRequests.add(requestMapper.toRequestDto(request));
                            eventRequests.remove(requestId);
                        } else if (participantLimit >= 1 && participantLimit > eventParticipants) {
                            request.setStatus(ParticipationRequestDto.StatusRequest.REJECTED);
                            rejectedRequests.add(requestMapper.toRequestDto(request));
                            --eventParticipants;
                            eventRequests.remove(requestId);
                        }
                        break;
                    default:
                        break;
                }
            } else {
                throw new NotValidatedExceptionConflict("Статус можно изменить только у заявок, находящихся в состоянии ожидания.");
            }
        }
        if (eventRequests.isEmpty()) {
            return eventRequestStatusUpdateResult;
        } else {
            Set<Map.Entry<Long, ParticipationRequest>> set = eventRequests.entrySet();

            for (Map.Entry<Long, ParticipationRequest> me : set) {
                var request = me.getValue();

                if (request.getStatus().equals(ParticipationRequestDto.StatusRequest.REJECTED)) {
                    rejectedRequests.add(requestMapper.toRequestDto(request));
                }

                if (request.getStatus().equals(ParticipationRequestDto.StatusRequest.CONFIRMED)) {
                    confirmedRequests.add(requestMapper.toRequestDto(request));
                }
                return eventRequestStatusUpdateResult;
            }
        }
        return eventRequestStatusUpdateResult;
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        List<ParticipationRequest> eventRequests = participationRequestRepository.findByRequester_Id(userId);
        return requestMapper.toListRequestDto(eventRequests);
    }
}
