package ru.practicum.ewm.error.exceptions;

public class EventBadTimeException extends RuntimeException{
    public EventBadTimeException(String message) {
        super(message);
    }
}
