package ru.practicum.mainservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@AllArgsConstructor
public class UserShortDto {

    private Long id;

    private String name;
}
