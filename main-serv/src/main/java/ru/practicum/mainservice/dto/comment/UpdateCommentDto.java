package ru.practicum.mainservice.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateCommentDto {

    @NotNull
    private Long id;

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(max = 200, message = "Имя не может быть длиннее 200 символов")
    private String textComment;
}
