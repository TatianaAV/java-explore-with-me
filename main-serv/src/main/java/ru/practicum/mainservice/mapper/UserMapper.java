package ru.practicum.mainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.dto.user.NewUser;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<UserDto> mapToUserDto(List<User> users);

    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(NewUser user);
}
