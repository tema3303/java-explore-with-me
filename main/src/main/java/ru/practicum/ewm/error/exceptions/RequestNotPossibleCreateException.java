package ru.practicum.ewm.error.exceptions;

public class RequestNotPossibleCreateException extends RuntimeException {
    public RequestNotPossibleCreateException(String message) {
        super(message);
    }
}
