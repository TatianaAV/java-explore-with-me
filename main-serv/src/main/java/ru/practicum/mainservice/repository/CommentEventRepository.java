package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.model.Event;

import java.util.Optional;

public interface CommentEventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventByIdAndState(Long eventId, EventFullDto.StateEvent stateEvent);
}