package ru.practicum.mainservice.dto.comment;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

public class NewCommentDto {
    @NotBlank(message = "Имя отсутствует")
    @Column(name = "name", nullable = false, unique = true)
    private String text;
}
