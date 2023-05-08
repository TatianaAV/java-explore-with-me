package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequest {
    private String annotation;

    private Long category;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Europe/Moscow")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateActionEnum stateAction;

    private String title;


    public enum StateActionEnum {
        PUBLISH_EVENT("PUBLISH_EVENT"),

        REJECT_EVENT("REJECT_EVENT");

        private final String value;

        StateActionEnum(String value) {
            this.value = value;
        }


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
