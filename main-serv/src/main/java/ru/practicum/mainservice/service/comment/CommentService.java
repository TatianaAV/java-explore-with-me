package ru.practicum.mainservice.service.comment;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentFullDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {

    @Transactional(readOnly = true)
    CommentDto getCommentById(Long commentId, HttpServletRequest request);

    @Transactional(readOnly = true)
    List<CommentDto> getCommentWithFilter(Long eventId, String text, Integer from, Integer size);

    CommentDto createComment(Long userId, NewCommentDto commentDto);

    @Transactional(readOnly = true)
    CommentFullDto getCommentUserById(Long commentId, Long userId);

    List<CommentFullDto> getAllUserById(Long userId, PageRequest pageRequest);

    CommentFullDto updateComment(Long userId, Long commentId, UpdateCommentDto updateCommentDto);

    void deleteComment(Long commentId, Long userId);

    CommentDto publishComment(Long commentId, String statusComment);

    CommentFullDto deleteCommentByAdmin(Long commentId);
}
