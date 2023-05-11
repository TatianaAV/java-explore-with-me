package ru.practicum.mainservice.dto.ParticipationRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Validated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    @Valid
    private List<Long> requestIds;
    private StatusEnum status;


    public enum StatusEnum {
        CONFIRMED("CONFIRMED"),

        REJECTED("REJECTED");

        private final String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }


        public static StatusEnum fromValue(String text) {
            for (StatusEnum b : StatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    public EventRequestStatusUpdateRequest addRequestIdsItem(Long requestIdsItem) {
        if (this.requestIds == null) {
            this.requestIds = new ArrayList<>();
        }
        this.requestIds.add(requestIdsItem);
        return this;
    }

    public EventRequestStatusUpdateRequest status(StatusEnum status) {
        this.status = status;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest = (EventRequestStatusUpdateRequest) o;
        return Objects.equals(this.requestIds, eventRequestStatusUpdateRequest.requestIds) &&
                Objects.equals(this.status, eventRequestStatusUpdateRequest.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestIds, status);
    }

    @Override
    public String toString() {

        return "class EventRequestStatusUpdateRequest {\n" +
                "    requestIds: " + toIndentedString(requestIds) + "\n" +
                "    status: " + toIndentedString(status) + "\n" +
                "}";
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
