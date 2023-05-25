package ru.practicum.mainservice.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewCommentDto {

    @NotNull
    private Long eventId;

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 4, max = 200, message = "Комментарий не может быть длиннее 200 символов")
    private String text;
}
