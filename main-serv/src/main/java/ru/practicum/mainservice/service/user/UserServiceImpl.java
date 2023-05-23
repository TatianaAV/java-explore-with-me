package ru.practicum.mainservice.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.mainservice.dto.event.EntenteParams;
import ru.practicum.mainservice.dto.user.NewUser;
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
    public UserDto createUser(NewUser createUser) {
        log.info("AdminServiceImpl/users/createUser {}", createUser);
        User user = userMapper.toUser(createUser);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getUsers(EntenteParams ententeParams) {

        List<Long> ids = ententeParams.getIds();
        Integer from = ententeParams.getFrom();
        Integer size = ententeParams.getSize();
      /*  List<User> users;
        if (ids == null) {
            users = userMapper.mapToUserDto(userRepository.findAll(PageRequest.of(from / size, size)));
        } else {
            users = userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size));
        }
        return userMapper.mapToUserDto(users);*/
        List<User> users;
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from / size, size, sortById);
        if (ids == null || CollectionUtils.isEmpty(ids)) {
            Page<User> usersPage = userRepository.findAll(page);
            users = usersPage.getContent();
        } else {
            users = userRepository.findAllByIdIn(ids, page);
        }
        return userMapper.mapToUserDto(users);
    }


    @Transactional
    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
