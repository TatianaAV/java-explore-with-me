package ru.practicum.mainservice.handler;

public class NotValidatedExceptionConflict extends RuntimeException {
    public NotValidatedExceptionConflict(String message) {
        super(message);
    }
}