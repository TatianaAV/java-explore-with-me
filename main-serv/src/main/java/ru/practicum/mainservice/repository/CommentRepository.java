package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.dto.comment.StateComment;
import ru.practicum.mainservice.model.Comment;
import ru.practicum.mainservice.model.User;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByIdAndCommentator(Long id, User user);

    Optional<Comment> findByIdAndCommentator_Id(Long commentId, Long userId);

    Optional<Comment> findByIdAndStateEquals(Long commentId, StateComment state);

    List<Comment> findAllByCommentator(User user, Pageable pageable);

    Optional<Comment> findByCommentator_IdAndEvent_Id(Long userId, Long eventId);
}