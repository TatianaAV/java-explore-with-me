package ru.practicum.mainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentFullDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.model.Comment;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommentMapper {

    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "commentator", source = "comment.commentator")
    @Mapping(target = "text", source = "comment.text")
    @Mapping(target = "state", source = "comment.state")
    @Mapping(target = "event", source = "comment.event")
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDtoList(List<Comment> comments);

    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "text", source = "comment.text")
    CommentFullDto toCommentFullDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "commentator", source = "user")
    @Mapping(target = "event", source = "event")
    @Mapping(target = "state", ignore = true)
    Comment toComment(NewCommentDto commentDto, User user, Event event);

    List<CommentFullDto> toCommentFullDtoList(List<Comment> comments);
}
