package ru.practicum.mainservice.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.event.EntenteParams;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.mapper.UserMapper;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto createUser(NewUserRequest createUser) {
        log.info("AdminServiceImpl/users/createUser {}", createUser);
        User user = userMapper.toUser(createUser);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getUsers(EntenteParams ententeParams) {

        List<Long> ids = ententeParams.getIds();
        Integer from = ententeParams.getFrom();
        Integer size = ententeParams.getSize();
        Page<User> users = userRepository.findAllByIdIn(ids, PageRequest.of(from, size));
        return userMapper.mapToUserDto(userMapper.mapToUserDto(users));
    }

    @Transactional
    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
