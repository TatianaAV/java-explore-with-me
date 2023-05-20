package ru.practicum.mainservice.repository;

import org.hibernate.query.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.dto.comment.StateComment;
import ru.practicum.mainservice.model.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Comment> findCommentWithFilter(Long eventId, String text, StateComment state, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> query = cb.createQuery(Comment.class);

        Root<Comment> root = query.from(Comment.class);
        List<Predicate> predicates = new ArrayList<>();

        if (eventId != null) {
            predicates.add(cb.or(cb.equal(root.get("event").get("id"), eventId)));
        }
        if (text != null) {
            predicates.add(cb.like(cb.upper(root.get("text")), "%" + text.toUpperCase() + "%"));
        }
        predicates.add(cb.and(cb.equal(root.get("state"), state)));
        query.where(predicates.toArray(new Predicate[]{}));

        Query<Comment> q = (Query<Comment>) entityManager.createQuery(query);
        q.setMaxResults(pageable.getPageSize());
        q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

        return q.getResultList();
    }
}

