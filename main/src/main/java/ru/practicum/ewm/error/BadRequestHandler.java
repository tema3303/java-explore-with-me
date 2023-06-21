package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.error.exceptions.EventBadTimeException;
import ru.practicum.ewm.error.exceptions.ValidationException;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Slf4j
public class BadRequestHandler {
    private final static String RESPONSE_STATUS = HttpStatus.BAD_REQUEST.name();
    private final static String REASON_BAD_REQUEST = "Incorrectly made request.";

    @ExceptionHandler
    public ApiError handleNotFoundException(final MethodArgumentTypeMismatchException e) {
        String message = e.getMessage();
        return ApiError.builder()
                .message(message)
                .reason(REASON_BAD_REQUEST)
                .status(RESPONSE_STATUS)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    public ApiError handleNotFoundException(final ValidationException e) {
        String message = e.getMessage();
        return ApiError.builder()
                .message(message)
                .reason(REASON_BAD_REQUEST)
                .status(RESPONSE_STATUS)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    public ApiError handleNotFoundException(final EventBadTimeException e) {
        String message = e.getMessage();
        return ApiError.builder()
                .message(message)
                .reason(REASON_BAD_REQUEST)
                .status(RESPONSE_STATUS)
                .timestamp(LocalDateTime.now())
                .build();
    }
}