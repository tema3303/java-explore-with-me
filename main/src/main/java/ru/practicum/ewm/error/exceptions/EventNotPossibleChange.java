package ru.practicum.ewm.error.exceptions;

public class EventNotPossibleChange extends RuntimeException {
    public EventNotPossibleChange(String message) {
        super(message);
    }
}