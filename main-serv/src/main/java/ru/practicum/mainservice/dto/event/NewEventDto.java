package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @NotBlank(message = "Краткое описание события. Не может быть пустым")
    @Size(min = 20, max = 2000, message = "Краткое описание события. Не может быть меньше : 20")
    private String annotation;

    @Positive
    private Long category;

    @NotBlank(message = "Полное описание события.  max: 7000, min: 20")
    @Size(min = 20, max = 7000,  message = "Полное описание события.  max: 7000, min: 20")
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Europe/Moscow")
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotNull(message = "Заголовок события.  max: 120, min: 3")
    private String title;
}

