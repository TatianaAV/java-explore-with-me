package ru.practicum.handler;

public class ErrorResponse {
    private final String error;

    ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
