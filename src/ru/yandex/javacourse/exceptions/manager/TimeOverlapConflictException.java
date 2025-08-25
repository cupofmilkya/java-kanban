package ru.yandex.javacourse.exceptions.manager;

public class TimeOverlapConflictException extends RuntimeException {
    public TimeOverlapConflictException(String message) {
        super(message);
    }
}
