package ru.practicum.mainservice.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewCommentDto {

    @NotNull
    private Long eventId;

    @NotBlank(message = "Комментарий отсутствует")
    private String text;
}
