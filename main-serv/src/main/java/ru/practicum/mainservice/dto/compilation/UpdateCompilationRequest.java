package ru.practicum.mainservice.dto.compilation;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class UpdateCompilationRequest {

    private List<Long> events;

    private Boolean pinned = false;

    @Size(max = 50, message = "Заголовок не может быть длиннее 50 символов")
    private String title;

}
