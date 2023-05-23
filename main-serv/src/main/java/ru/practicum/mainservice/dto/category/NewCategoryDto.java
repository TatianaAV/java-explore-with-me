package ru.practicum.mainservice.dto.category;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NewCategoryDto {

    @NotBlank
    @Size(max = 50, message = "Название категории от 10 до 50 символов")
    private String name;
}
