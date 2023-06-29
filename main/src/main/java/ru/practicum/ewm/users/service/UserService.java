package ru.practicum.ewm.users.service;


import ru.practicum.ewm.users.model.dto.UserDto;
import ru.practicum.ewm.users.model.dto.UserRateDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> usersId, Integer from, Integer size);

    UserDto addUser(UserDto userDto);

    void deleteUser(long userId);

    List<UserRateDto> getTopUsers(Integer size, String sort);
}