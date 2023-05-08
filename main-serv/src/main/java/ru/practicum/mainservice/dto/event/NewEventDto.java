package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Validated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
  public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

   // @Max(3000)
    @NotNull(message = "Краткое описание события.  max: 2000, min: 20")
    private String annotation;
    @Positive
    private Long category;

  //  @Max(9000)
    @NotNull(message = "Полное описание события.  max: 7000, min: 20")
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Europe/Moscow")
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @NotNull
    private Boolean paid;

    private Integer participantLimit;

    /* requestModeration	boolean
     *   example: false
     *   default: true
     *  Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события.
     *  Если false - то будут подтверждаться автоматически.
     */
    private Boolean requestModeration;

  // @Max(220)
   @NotNull(message = "Заголовок события.  max: 120, min: 3")
    private String title;

 /* public void setEventDate(String eventDate) {
        this.eventDate = LocalDateTime.parse(URLDecoder.decode(eventDate, StandardCharsets.UTF_8), FORMAT);
    }*/
}

