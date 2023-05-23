package ru.practicum.mainservice.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NewUser {

    @Email(message = "Email не соответствует формату", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "Email не может быть пустым")
    @Size(min = 6, max = 254, message = "Email не может быть длиннее 254 символов")
    private String email;

    @Size(min = 2, max = 250, message = "Имя не может быть длиннее 200 символов")
    @NotBlank(message = "Имя отсутствует")
    private String name;
}