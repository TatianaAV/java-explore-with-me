package ru.practicum.mainservice.repository;

import org.hibernate.query.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.dto.event.EntenteParams;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.handler.ValidationException;
import ru.practicum.mainservice.model.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.mainservice.dto.event.EventFullDto.StateEvent.PUBLISHED;
import static ru.practicum.mainservice.dto.event.EventFullDto.StateEvent.fromValue;

@Repository
public class EventCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Event> findEventByAdminWithFilter(EntenteParams ententeParams) {
        List<Long> ids = ententeParams.getIds();
        Integer from = ententeParams.getFrom();
        Integer size = ententeParams.getSize();
        EventFullDto.StateEvent state = fromValue(ententeParams.getText());
        Long idCategory = ententeParams.getCategories();
        LocalDateTime rangeStart = ententeParams.getRangeStart();
        LocalDateTime rangeEnd = ententeParams.getRangeEnd();
        Pageable pageable = PageRequest.of(from / size, size);

        validateDataEvent(rangeStart, rangeEnd);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);

        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if (ids != null) {
            predicates.add(cb.or(cb.in(root.get("initiator").get("id")).value(ids), cb.equal(root.get("state"), state)));
        }

        if (state != null) {
            predicates.add(cb.or(cb.equal(root.get("state"), state)));
        }
        if (idCategory != null) {
            predicates.add(cb.equal(root.get("category").get("id"), idCategory));
        }
        if (rangeStart != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }

        if (rangeEnd != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }

        query.where(predicates.toArray(new Predicate[]{}));

        Query<Event> q = (Query<Event>) entityManager.createQuery(query);
        q.setMaxResults(pageable.getPageSize());
        q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

        return q.getResultList();
    }

    public List<Event> findByEventUserWithFilter(String text, Long idCategory, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, String sort, PageRequest pageable) {

        validateDataEvent(rangeStart, rangeEnd);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);

        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (text != null) {
            //predicates.add(cb.like(cb.lower(root.get("title")), "%" + text.toLowerCase() + "%"));
            predicates.add(cb.like(cb.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"));
        }

        if (idCategory != null) {
            predicates.add(cb.equal(root.get("category").get("id"), idCategory));
        }

        if (paid != null) {
            predicates.add(cb.equal(root.get("paid"), paid));
        }

        if (rangeStart != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }

        if (rangeEnd != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }

        predicates.add(cb.equal(root.get("state"), PUBLISHED));

        if (sort != null && sort.equalsIgnoreCase("EVENT_DATE")) {
            query.orderBy(cb.asc(root.get("eventDate"))); // cb.desc() for descending order
        }

        query.where(predicates.toArray(new Predicate[]{}));

        Query<Event> q = (Query<Event>) entityManager.createQuery(query);
        q.setMaxResults(pageable.getPageSize());
        q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        return q.getResultList();
    }

    private static void validateDataEvent(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Дата начала позже даты конца");
        }
    }
}


