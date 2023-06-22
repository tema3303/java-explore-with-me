package ru.practicum.ewm.stats.server.error;

public class BadTimeException extends RuntimeException{
    public BadTimeException(String message) {
        super(message);
    }
}
