package ru.practicum.mainservice.service.user;

import ru.practicum.mainservice.dto.event.EntenteParams;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(EntenteParams ententeParams);

    UserDto createUser(NewUserRequest user);

    void deleteUserById(Long userId);
}
