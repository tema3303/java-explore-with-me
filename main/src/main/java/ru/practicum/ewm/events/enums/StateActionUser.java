package ru.practicum.ewm.events.enums;

import ru.practicum.ewm.error.exceptions.ValidationException;

public enum StateActionUser {
    SEND_TO_REVIEW, CANCEL_REVIEW;

    public static StateActionUser fromName(String name) {
        if (name == null) return null;
        try {
            return StateActionUser.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException(name);
        }
    }
}
