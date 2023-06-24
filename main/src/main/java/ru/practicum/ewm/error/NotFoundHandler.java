package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.error.exceptions.NotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseStatus(HttpStatus.NOT_FOUND)
@Slf4j
public class NotFoundHandler {

    private static final String REASON = "The required object was not found.";

    @ExceptionHandler
    public ApiError handleNotFoundException(final NotFoundException e) {
        String message = e.getMessage();
        return ApiError.builder()
                .message(message)
                .reason(REASON)
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}