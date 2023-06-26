package ru.practicum.ewm.users.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.users.model.dto.UserDto;
import ru.practicum.ewm.users.model.dto.UserRateDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static UserRateDto toUserRateDto(User user) {
        return UserRateDto.builder()
                .id(user.getId())
                .name(user.getName())
                .rate(user.getRate())
                .build();
    }
}