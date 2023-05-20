package ru.practicum.mainservice.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.mainservice.dto.user.UserShortDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDto {

    private Long id;

    @NotNull
    private CommentDto.EventDto event;


    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Europe/Moscow")
    private LocalDateTime created;

    private UserShortDto commentator;

    private StateComment state;

    /**
     * DTO for {@link ru.practicum.mainservice.model.Event}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventDto implements Serializable {
        private Long id;
        private String title;
    }
}

