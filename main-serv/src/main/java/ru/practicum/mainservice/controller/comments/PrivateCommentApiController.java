package ru.practicum.mainservice.controller.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentFullDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;
import ru.practicum.mainservice.service.comment.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
        log.info("GET PrivateCommentApiController/ getEventFullById/ eventId {}", commentId);
        return commentService.getCommentById(commentId, request);
    }


    /**
     * 2. Получение комментариев к событию с фильтрацией любым пользователем
     */
    @GetMapping("/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentWithFilter(@RequestParam(required = false) Long eventId,
                                                 @RequestParam(name = "text", required = false) String text,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                 HttpServletRequest request) {
        log.info("GET PrivateCommentApiController/ getEventsWithFilter");

        log.info("eventId: {}", eventId);
        log.info("text: {}", text);
        log.info("from: {}", from);
        log.info("size: {}", size);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}, {}, {}", request.getProtocol(), request.getHttpServletMapping(), request.getRequestURI());

        return commentService.getCommentWithFilter(eventId, text, from, size);
    }

    /**
     * 3. Добавление комментария к событию авторизованным пользователем
     * с обязательным одобрением администратора.
     * При отсутствии обязательных параметров ошибка
     */
    @PostMapping("/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestParam Long userId,
                                 @Valid @RequestBody NewCommentDto comment,
                                 HttpServletRequest request) {
        log.info("POST PrivateCommentApiController/ addComment/ userId {}", userId);
        log.info("POST PrivateCommentApiController/ addComment/ userId {}", userId);
        log.info("PATH {}, queryString {}", request.getRequestURI(), request.getQueryString());
        return commentService.createComment(userId, comment);
    }

    /**
     * 4. Получение
     * CommentFullDto комментария пользователем
     */
    @GetMapping("/comment/{commentId}/user")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto getCommentUserById(@PathVariable Long commentId,
                                             @RequestParam Long userId) {
        log.info("GET PrivateCommentApiController/ getCommentUserById/ commentId {}", commentId);
        log.info("GET PrivateCommentApiController/ getCommentUserById/ userId {}", userId);
        return commentService.getCommentUserById(commentId, userId);
    }

    /**
     * 5. Получение
     * CommentFullDto всех комментариев добавленных текущим пользователем
     */
    @GetMapping("/comment/user")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> getCommentUserById(@RequestParam Long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("GET PrivateCommentApiController/ getCommentUserById/ userId {}", userId);
        return commentService.getAllUserById(userId, PageRequest.of(from / size, size));
    }

    /**
     * 6. Обновление комментария к событию авторизованным пользователем с обязательным одобрением администратора
     */

    @PatchMapping("comment/{commentId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto updateComment(@Valid @RequestBody UpdateCommentDto updateCommentDto,
                                    @PathVariable Long commentId,
                                    @PathVariable Long userId) {
        return commentService.updateComment(userId, commentId, updateCommentDto);
    }

    /**
     * 7. Удаление комментария пользователем
     */
    @DeleteMapping("comment/{commentId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId,
                                  @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
    }

    /**
     * 8. Подтверждение публикации комментария к событию администратором
     */
    @PatchMapping("comment/{commentId}/admin")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto publishComment(@RequestParam String state,
                                     @PathVariable Long commentId) {
        return commentService.publishComment(commentId, state);
    }

    /**
     * 9. Удаление комментария администратором
     */
    @PatchMapping("comment/{commentId}/admin/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CommentFullDto deleteCommentByAdmin(@PathVariable Long commentId) {
        return commentService.deleteCommentByAdmin(commentId);
    }
}