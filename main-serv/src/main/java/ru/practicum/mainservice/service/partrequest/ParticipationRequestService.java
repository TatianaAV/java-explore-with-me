package ru.practicum.mainservice.service.partrequest;

import ru.practicum.mainservice.dto.ParticipationRequest.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.dto.ParticipationRequest.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.ParticipationRequest.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    EventRequestStatusUpdateResult changeRequestStatus(EventRequestStatusUpdateRequest body, Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequests(Long userId);
}
