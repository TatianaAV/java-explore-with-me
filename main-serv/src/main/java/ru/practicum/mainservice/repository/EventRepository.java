package ru.practicum.mainservice.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStateOrderByEventDateDesc(EventFullDto.StateEvent stateEvent, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', ?2, '%')) " +
            "and e.category.id = ?3 " +
            "and e.paid = ?4 " +
            "and e.eventDate BETWEEN ?5 AND ?6")
    List<Event> findByEventWithFilter(@Nullable String title, @Nullable String annotation, @Nullable Long idCategory, @Nullable Boolean paid, @Nullable LocalDateTime eventDateStart, @Nullable LocalDateTime eventDateEnd, PageRequest pageable);

    List<Event> findByIdIn(List<Long> ids);

    List<Event> findEventsByCategory_Id(Long id);

    List<Event> findAllByInitiator_IdOrderByEventDateAsc(Long userId, Pageable pageable);

    Event findEventByIdAndInitiator(Long eventId, User user);

    Optional<Event> findEventByIdAndInitiator_IdAndStateNotOrEventDateBefore(Long eventId, Long userId, EventFullDto.StateEvent state, LocalDateTime eventDate);
}