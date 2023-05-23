package ru.practicum.mainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.practicum.mainservice.dto.user.NewUser;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.dto.user.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<User> mapToUserDto(Page<User> users);

    List<UserDto> mapToUserDto(List<User> users);

    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(NewUser user);
}
