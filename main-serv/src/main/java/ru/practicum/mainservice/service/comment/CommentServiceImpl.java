package ru.practicum.mainservice.service.comment;

import dto.EndpointDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.comment.*;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.handler.NotFoundException;
import ru.practicum.mainservice.handler.NotValidatedExceptionConflict;
import ru.practicum.mainservice.mapper.CommentMapper;
import ru.practicum.mainservice.model.Comment;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.*;
import ru.practicum.statsclientapp.statsclient.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final CommentEventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentCustomRepository customRepository;
    private final CommentMapper commentMapper;
    private final StatsClient statsClient;

    @Transactional(readOnly = true)
    @Override
    public CommentDto getCommentById(Long commentId, HttpServletRequest request) {
        log.info("CommentServiceImpl/getCommentById/commentId {}", commentId);
        Comment comment = commentRepository.findByIdAndStateEquals(commentId, StateComment.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Комментарий еще не опубликован."));
        sendStatistics(request);
        return commentMapper.toCommentDto(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getCommentWithFilter(Long eventId, String text, Integer from, Integer size, HttpServletRequest request) {
        log.info("CommentServiceImpl/getCommentWithFilter/eventId {}", eventId);
        log.info("CommentServiceImpl/getCommentWithFilter/from {}", from);
        log.info("CommentServiceImpl/getCommentWithFilter/size {}", size);
        sendStatistics(request);
        List<Comment> commentDtoList = customRepository.findCommentWithFilter(eventId, text, StateComment.PUBLISHED, from, size);
        return commentMapper.toCommentDtoList(commentDtoList);
    }

    /**
     * У одного события один комментарий от пользователя.
     * * При попытке создать еще один комментарий ошибка с номером уже существующего комментария
     * * (или сделать переадресацию на обновление существующего).
     */
    @Override
    public CommentDto createComment(Long userId, NewCommentDto commentDto) {
        log.info("CommentServiceImpl/createComment/userId {}", userId);
        log.info("CommentServiceImpl/createComment/eventId {}", commentDto.getEventId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден. Войдите или зарегистрируйтесь."));
        Event event = eventRepository.findEventByIdAndState(commentDto.getEventId(), EventFullDto.StateEvent.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие не найдено."));
        if (user.equals(event.getInitiator())) {
            throw new NotValidatedExceptionConflict("Вы не можете писать комментарии к своим событиям");
        }
        Optional<Comment> commentExist = commentRepository.findByCommentator_IdAndEvent_Id(userId, commentDto.getEventId())
                .stream().findFirst();
        if (commentExist.isPresent()) {
            throw new NotValidatedExceptionConflict("Вы уже написали комментарий " + commentExist.get().getId() + " к событию");
        }

        Comment comment = commentMapper.toComment(commentDto, user, event);
        comment.setState(StateComment.PENDING);
        comment.setCreated(LocalDateTime.now());
        comment.setUpdate(false);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(savedComment);
    }

    @Transactional(readOnly = true)
    @Override
    public CommentFullDto getCommentUserById(Long commentId, Long userId) {
        log.info("CommentServiceImpl/getCommentUserById/userId {}", userId);
        log.info("CommentServiceImpl/getCommentUserById/commentId {}", commentId);
        CommentFullDto comment = commentMapper.toCommentFullDto(commentRepository.findByIdAndCommentator_Id(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден.")));
        CommentStatisticsGet.addViewsToComments(List.of(comment), statsClient);
        return comment;
    }

    @Override
    public List<CommentFullDto> getAllUserById(Long userId, PageRequest pageRequest) {
        log.info("CommentServiceImpl/getAllUserById/commentId {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден. Войдите или зарегистрируйтесь."));
        List<CommentFullDto> comments = commentMapper.toCommentFullDtoList(commentRepository.findAllByCommentator(user, pageRequest));
        CommentStatisticsGet.addViewsToComments(comments, statsClient);
        return comments;
    }

    @Override
    public CommentFullDto updateComment(Long userId, Long commentId, UpdateCommentDto updateCommentDto) {
        log.info("CommentServiceImpl/update/commentId {} created", commentId);
        log.info("CommentServiceImpl/update/userId {} created", userId);
        Comment comment = commentRepository.findByIdAndCommentator_Id(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден."));

        if (updateCommentDto == null) {
            return commentMapper.toCommentFullDto(comment);
        }
        if (updateCommentDto.getText() != null) {
            comment.setText(updateCommentDto.getText());
            comment.setState(StateComment.PENDING);
            comment.setCreated(LocalDateTime.now());
            comment.setUpdate(true);
        }

        return commentMapper.toCommentFullDto(comment);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        log.info("CommentServiceImpl/deleteComment/userId {}", userId);
        log.info("CommentServiceImpl/deleteComment/commentId {}", commentId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден. Войдите или зарегистрируйтесь."));
        commentRepository.deleteByIdAndCommentator(commentId, user);
    }

    @Override
    public CommentDto publishComment(Long commentId, String statusComment) {
        log.info("CommentServiceImpl/publishComment/statusComment {}", statusComment);
        log.info("CommentServiceImpl/publishComment/commentId {}", commentId);
        Comment comment = commentRepository.findByIdAndStateEquals(commentId, StateComment.PENDING)
                .orElseThrow(() -> new NotValidatedExceptionConflict("Комментарий не найден или уже обработан."));
        StateComment stateComment = StateComment.fromValue(statusComment);
        comment.setState(stateComment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public CommentFullDto deleteCommentByAdmin(Long commentId) {
        log.info("CommentServiceImpl/deleteCommentByAdmin/commentId {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий удален или не найден"));
        comment.setState(StateComment.CANCELED);
        return commentMapper.toCommentFullDto(comment);
    }

    private void sendStatistics(HttpServletRequest request) {
        String appName = "ewm";
        statsClient.addEndpointHit(new EndpointDto(
                appName,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        ));
    }
}
