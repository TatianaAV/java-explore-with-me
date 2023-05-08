package ru.practicum.mainservice.dto.compilation;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {

    private List<Long> events;

    //example: false
    //default: false
    //Закреплена ли подборка на главной странице сайта
    private Boolean pinned;

    @NotBlank(message = "Имя отсутствует")
    @Size(max = 200, message = "Имя не может быть длиннее 200 символов")
    private String title;
}
