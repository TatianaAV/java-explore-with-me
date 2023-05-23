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

    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDtoList(List<Comment> comments);

    CommentFullDto toCommentFullDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    Comment toComment(NewCommentDto commentDto, User commentator, Event event);

    List<CommentFullDto> toCommentFullDtoList(List<Comment> comments);
}
