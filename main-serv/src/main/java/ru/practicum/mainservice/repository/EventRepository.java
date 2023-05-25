package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByIdAndState(Long id, EventFullDto.StateEvent state);

    List<Event> findByIdIn(List<Long> ids);

    List<Event> findEventsByCategory_Id(Long id);

    List<Event> findAllByInitiator_IdOrderByEventDateAsc(Long userId, Pageable pageable);

    Event findEventByIdAndInitiator(Long eventId, User user);

    Optional<Event> findEventByIdAndInitiator_Id(Long eventId, Long userId);
}