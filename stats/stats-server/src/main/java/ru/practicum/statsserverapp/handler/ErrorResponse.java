package ru.practicum.statsserverapp.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final String error;
    private final HttpStatus status;
    private final StackTraceElement[] stackTrace;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Europe/Moscow")
    private final LocalDateTime timestamp = LocalDateTime.now();



    ErrorResponse(String error, HttpStatus status, StackTraceElement[] stackTrace) {
        this.error = error;
        this.status = status;
        this.stackTrace = stackTrace;
    }

    public String getError() {
        return error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }
}

