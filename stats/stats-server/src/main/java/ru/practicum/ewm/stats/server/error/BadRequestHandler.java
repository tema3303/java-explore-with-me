package ru.practicum.ewm.stats.server.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.error.ApiError;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Slf4j
public class BadRequestHandler {
    private static final String RESPONSE_STATUS = HttpStatus.BAD_REQUEST.name();
    private static final String REASON_BAD_REQUEST = "Incorrectly made request.";

    @ExceptionHandler
    public ApiError handleNotFoundException(final BadTimeException e) {
        String message = e.getMessage();
        return ApiError.builder()
                .message(message)
                .reason(REASON_BAD_REQUEST)
                .status(RESPONSE_STATUS)
                .timestamp(LocalDateTime.now())
                .build();
    }
}