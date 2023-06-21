package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.error.exceptions.CategoryNameDoubleException;
import ru.practicum.ewm.error.exceptions.EventNotPossibleChange;
import ru.practicum.ewm.error.exceptions.RequestNotPossibleCreateException;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseStatus(HttpStatus.CONFLICT)
@Slf4j
public class ConflictHandler {

    private final static String RESPONSE_STATUS_CONFLICT = HttpStatus.CONFLICT.name();
    private final static String RESPONSE_STATUS_FORBIDDEN = HttpStatus.FORBIDDEN.name();
    private final static String REASON_INTEGRITY_CONFLICT = "Integrity constraint has been violated.";
    private final static String REASON_INTEGRITY_FORBIDDEN = "For the requested operation the conditions are not met.";


    @ExceptionHandler
    public ApiError handleNotFoundException(final DataIntegrityViolationException e) {
        String message = e.getMessage();
        return ApiError.builder()
                .message(message)
                .reason(REASON_INTEGRITY_CONFLICT)
                .status(RESPONSE_STATUS_CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    public ApiError handleNotPossibleChange(final EventNotPossibleChange e) {
        String message = e.getMessage();
        return ApiError.builder()
                .message(message)
                .reason(REASON_INTEGRITY_FORBIDDEN)
                .status(RESPONSE_STATUS_FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    public ApiError handleNotFoundException(final RequestNotPossibleCreateException e) {
        String message = e.getMessage();
        return ApiError.builder()
                .message(message)
                .reason(REASON_INTEGRITY_CONFLICT)
                .status(RESPONSE_STATUS_CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    public ApiError handleNotFoundException(final CategoryNameDoubleException e) {
        String message = e.getMessage();
        return ApiError.builder()
                .message(message)
                .reason(REASON_INTEGRITY_CONFLICT)
                .status(RESPONSE_STATUS_CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }


}