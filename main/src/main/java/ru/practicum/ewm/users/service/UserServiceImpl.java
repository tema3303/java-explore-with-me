package ru.practicum.ewm.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.error.exceptions.NotFoundException;
import ru.practicum.ewm.error.exceptions.ValidationException;
import ru.practicum.ewm.users.model.dto.UserRateDto;
import ru.practicum.ewm.users.repository.UserRepository;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.model.UserMapper;
import ru.practicum.ewm.users.model.dto.UserDto;

import java.util.List;
import java.util.Objects;
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
            if (Objects.isNull(from) || Objects.isNull(size)) {
                throw new ValidationException("Значения не могут быть null");
            }
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
        user.setRate(0);
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

    @Override
    public List<UserRateDto> getTopUsers(Integer size, String sort) {
        Pageable pagination;
        if (sort.equals("ASC")) {
            pagination = PageRequest.of(0, size, Sort.by("rate").ascending());
        } else if (sort.equals("DESC")) {
            pagination = PageRequest.of(0, size, Sort.by("rate").descending());
        } else {
            throw new ValidationException("Неккоректное правило сортировки");
        }
        List<User> resultEvents = userRepository.findAll(pagination).getContent();
        return resultEvents.stream()
                .map(UserMapper::toUserRateDto)
                .collect(Collectors.toList());
    }
}