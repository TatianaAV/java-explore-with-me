package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.model.Location;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {

    @Size(min = 20, max = 2000,  message = "Полное описание события.  max: 7000, min: 20")
    private String annotation;

    @Positive
    private Long category;

    @Size(min = 20, max = 7000,  message = "Полное описание события.  max: 7000, min: 20")
    private String description;


    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Europe/Moscow")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateActionEnum stateAction;

    @Size(min = 20, max = 120,  message = "Заголовок события.  max: 120, min: 20")
    private String title;


    public enum StateActionEnum {
        SEND_TO_REVIEW("SEND_TO_REVIEW"),

        CANCEL_REVIEW("CANCEL_REVIEW"),

        PUBLISH_EVENT("PUBLISH_EVENT"),

        REJECT_EVENT("REJECT_EVENT");


        private final String value;

        StateActionEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static StateActionEnum fromValue(String text) {
            for (StateActionEnum b : StateActionEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }
}
