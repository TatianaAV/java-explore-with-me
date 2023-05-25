package ru.practicum.mainservice.dto.ParticipationRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    @Valid
    private List<ParticipationRequestDto> confirmedRequests;

    @Valid
    private List<ParticipationRequestDto> rejectedRequests;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = (EventRequestStatusUpdateResult) o;
        return Objects.equals(this.confirmedRequests, eventRequestStatusUpdateResult.confirmedRequests) &&
                Objects.equals(this.rejectedRequests, eventRequestStatusUpdateResult.rejectedRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmedRequests, rejectedRequests);
    }

    @Override
    public String toString() {

        return "class EventRequestStatusUpdateResult {\n" +
                "    confirmedRequests: " + toIndentedString(confirmedRequests) + "\n" +
                "    rejectedRequests: " + toIndentedString(rejectedRequests) + "\n" +
                "}";
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
