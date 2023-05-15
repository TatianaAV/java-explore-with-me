package ru.practicum.mainservice.controller.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;
import ru.practicum.mainservice.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PrivateCommentApiController {
    private final CommentService commentService;

    /**
     * 1. Получение комментария к событию любым пользователем
     */
    @GetMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentById(@PathVariable Long commentId,
                                     HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        log.info("GET PublicApiController/ getEventFullById/ eventId {}", commentId);
        return commentService.getCommentById(commentId, request);
    }


    /**
     * 2. Получение комментариев к событию с фильтрацией любым пользователем
     */
    @GetMapping("/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentWithFilter(@RequestParam(name = "text", required = false) String text,
                                                 @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                 @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                 @RequestParam(name = "sort", required = false) String sort,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                 HttpServletRequest request) {
        log.info("GET PublicApiController/ getEventsWithFilter");

        log.info("text: {}", text);
        log.info("sort: {}", sort);
        log.info("from: {}", from);
        log.info("size: {}", size);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}, {}, {}", request.getProtocol(), request.getHttpServletMapping(), request.getRequestURI());

        return commentService.getCommentWithFilter(text, rangeStart, rangeEnd, sort, from, size);
    }

    /**
     * 3. Добавление комментария к событию авторизованным пользователем с обязательным одобрением администратора
     */
    @PostMapping("/comment/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Long userId,
                                 @RequestParam Long eventId,
                                 @RequestParam NewCommentDto comment,
                                 HttpServletRequest request) {
        log.info("POST PrivateApiController/ addParticipationRequest/ userId {}", userId);
        log.info("PATH {}, queryString {}", request.getRequestURI(), request.getQueryString());
        return commentService.createComment(userId, eventId, comment);
    }

    /**
     * 4. Обновление комментария к событию авторизованным пользователем с обязательным одобрением администратора
     */

    @PatchMapping("comment/{commentId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@RequestParam UpdateCommentDto updateCommentDto,
                                    @PathVariable String commentId,
                                    @PathVariable String userId) {
        return commentService.updateComment(commentId, userId, updateCommentDto);
    }

    /**
     * 5. Удаление комментария пользователем
     */
    @DeleteMapping("comment/{commentId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCommentById(@PathVariable String commentId,
                                  @RequestParam String userId) {
        commentService.deleteComment(commentId, userId);
    }

    /**
     * 6. Подтверждение публикации комментария к событию администратором
     */
    @PatchMapping("comment/admin/status")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto changeStatus(@RequestParam String statusComment,
                                   @RequestParam Long commentId) {
        return commentService.changeCommentByAdmin(commentId, statusComment);
    }

    /**
     * 7. Подтверждение публикации комментария к событию администратором
     */
    @DeleteMapping("comment/{commentId}/admin/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCommentByAdmin(@PathVariable String commentId) {
        commentService.deleteCommentByAdmin(commentId);
    }
}