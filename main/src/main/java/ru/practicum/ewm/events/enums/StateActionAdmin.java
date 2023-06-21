package ru.practicum.ewm.events.enums;

import ru.practicum.ewm.error.exceptions.ValidationException;

public enum StateActionAdmin {
    PUBLISH_EVENT, REJECT_EVENT;

    public static StateActionAdmin fromName(String name) {
        if (name == null) return null;
        try {
            return StateActionAdmin.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException(name);
        }
    }
}