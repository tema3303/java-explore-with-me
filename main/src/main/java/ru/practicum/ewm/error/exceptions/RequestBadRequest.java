package ru.practicum.ewm.error.exceptions;

public class RequestBadRequest extends RuntimeException {
    public RequestBadRequest(String message) {
        super(message);
    }
}