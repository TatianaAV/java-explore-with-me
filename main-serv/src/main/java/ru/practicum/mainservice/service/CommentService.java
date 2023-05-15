package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {
    CommentDto getCommentById(Long commentId, HttpServletRequest request);

    List<CommentDto> getCommentWithFilter(String text, String rangeStart, String rangeEnd, String sort, Integer from, Integer size);

    CommentDto createComment(Long userId, Long eventId, NewCommentDto comment);

    CommentDto changeCommentByAdmin(Long commentId, String statusComment);

    CommentDto updateComment(String commentId, String userId, UpdateCommentDto updateCommentDto);

    void deleteComment(String commentId, String userId);

    void deleteCommentByAdmin(String commentId);
}
