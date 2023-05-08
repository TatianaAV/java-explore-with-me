package ru.practicum.mainservice.dto.ParticipationRequest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto { //Заявка на участие в событии
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Europe/Moscow")
    private LocalDateTime created;

    private Long event;

    private Long requester;

    private StatusRequest status;


    public enum StatusRequest {
        PENDING("PENDING"),
        CONFIRMED("CONFIRMED"),
        CANCELED("CANCELED"),
        REJECTED("REJECTED");

        private final String value;

        StatusRequest(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static StatusRequest fromValue(String text) {
            for (StatusRequest b : StatusRequest.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }
}

