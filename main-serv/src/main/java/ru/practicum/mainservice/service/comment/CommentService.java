package ru.practicum.mainservice.service.comment;

import org.springframework.data.domain.PageRequest;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentFullDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {

    CommentDto getCommentById(Long commentId, HttpServletRequest request);

    List<CommentDto> getCommentWithFilter(Long eventId, String text, Integer from, Integer size, HttpServletRequest request);

    CommentDto createComment(Long userId, NewCommentDto commentDto);

    CommentFullDto getCommentUserById(Long commentId, Long userId);

    List<CommentFullDto> getAllUserById(Long userId, PageRequest pageRequest);

    CommentFullDto updateComment(Long userId, Long commentId, UpdateCommentDto updateCommentDto);

    void deleteComment(Long commentId, Long userId);

    CommentDto publishComment(Long commentId, String statusComment);

    CommentFullDto deleteCommentByAdmin(Long commentId);
}
