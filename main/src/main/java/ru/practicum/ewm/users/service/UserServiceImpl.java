package ru.practicum.ewm.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.error.exceptions.NotFoundException;
import ru.practicum.ewm.users.repository.UserRepository;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.model.UserMapper;
import ru.practicum.ewm.users.model.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> usersId, Integer from, Integer size) {
        List<User> users;
        if (usersId == null) {
            Pageable pagination = PageRequest.of(from / size, size);
            users = userRepository.findAll(pagination).getContent();
        } else {
            users = userRepository.findAllByIdIn(usersId);
        }
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User", userId);
        }
        userRepository.deleteById(userId);
    }
}