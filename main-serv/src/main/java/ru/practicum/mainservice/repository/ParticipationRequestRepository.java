package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.practicum.mainservice.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Optional<ParticipationRequest> findByIdAndRequester_Id(Long requestId, Long userId);

    List<ParticipationRequest> findParticipationRequestByEvent_Initiator_IdAndEvent_Id(@NonNull Long userId, Long eventId);

    List<ParticipationRequest> findByEvent_IdAndEvent_Initiator_Id(Long eventId, Long initiatorId);

    @Query("select count(pr.requester) from ParticipationRequest as pr where pr.event.id = ?1 and pr.status = 'CONFIRMED'")
    Integer findByEvent_IdCount(Long eventId);

    List<ParticipationRequest> findByRequester_Id(Long id);

    @Query("select pr from ParticipationRequest as pr where pr.event.id in :eventIds and pr.status = 'CONFIRMED'")
    List<ParticipationRequest> findAllConfirmedByEventIdIn(List<Long> eventIds);
}