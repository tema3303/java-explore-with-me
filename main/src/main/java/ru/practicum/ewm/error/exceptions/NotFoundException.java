package ru.practicum.ewm.error.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String object, Long id) {
        super(String.format("%s with id=%s was not found", object, id));
    }


}
