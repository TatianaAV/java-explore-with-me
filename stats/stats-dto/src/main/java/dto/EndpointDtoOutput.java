package dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndpointDtoOutput {
    private String app;
    private String uri;
    private long hits;
}