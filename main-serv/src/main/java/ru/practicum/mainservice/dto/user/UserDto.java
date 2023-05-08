package ru.practicum.mainservice.dto.user;


import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Valid
@Getter
@Setter
public class UserDto {
    private Long id;

    private String email;

    private String name;
}
