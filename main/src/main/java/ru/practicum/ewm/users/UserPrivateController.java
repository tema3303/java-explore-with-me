package ru.practicum.ewm.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.users.model.dto.UserRateDto;
import ru.practicum.ewm.users.service.UserService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/top")
@RequiredArgsConstructor
@Slf4j
public class UserPrivateController {

    private final UserService userService;

    @GetMapping
    public List<UserRateDto> getTopUsers(@RequestParam(defaultValue = "10") @Positive Integer size,
                                         @RequestParam(defaultValue = "DESC") String sort) {
        return userService.getTopUsers(size, sort);
    }
}