package ru.practicum.mainservice.handler;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
