package ru.practicum.ewm.users.service;


import ru.practicum.ewm.users.model.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(Integer[] usersId, Integer from, Integer size);

    UserDto addUser(UserDto userDto);

    void deleteUser(long userId);
}