package ru.practicum.mainservice.dto.compilation;

import lombok.*;
import ru.practicum.mainservice.dto.event.EventShortDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
  private Long id;
  private List<EventShortDto> events = new ArrayList<>();
  private Boolean pinned;
  private String title;

  public CompilationDto(Long id, Boolean pined, String title) {
    this.id = id;
    this.pinned = pined;
    this.title = title;
  }
}