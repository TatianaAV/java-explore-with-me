package ru.practicum.mainservice.dto.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ApiError {

    private List<String> errors;

    private String message;

    private String reason;
}