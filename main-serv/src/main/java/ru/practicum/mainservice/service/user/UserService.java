package ru.practicum.mainservice.service.user;

import ru.practicum.mainservice.dto.event.EntenteParams;
import ru.practicum.mainservice.dto.user.NewUser;
import ru.practicum.mainservice.dto.user.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(EntenteParams ententeParams);

    UserDto createUser(NewUser user);

    void deleteUserById(Long userId);
}
