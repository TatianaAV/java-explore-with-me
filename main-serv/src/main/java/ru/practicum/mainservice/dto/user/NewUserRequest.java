package ru.practicum.mainservice.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Valid
@Getter
@Setter
public class NewUserRequest {
    @Email(message = "Email не соответствует формату")
    @NotBlank(message = "Email не может быть пустым")
    @Size(max = 50, message = "Email не может быть длиннее 50 символов")
    private String email;

    @Size(max = 200, message = "Имя не может быть длиннее 200 символов")
    @NotBlank(message = "Имя отсутствует")
    private String name;
}