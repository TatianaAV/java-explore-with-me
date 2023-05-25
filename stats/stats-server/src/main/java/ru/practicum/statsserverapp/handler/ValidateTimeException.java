package ru.practicum.statsserverapp.handler;

public class ValidateTimeException extends RuntimeException {
    public ValidateTimeException(String message) {
        super(message);
    }
}