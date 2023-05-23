package ru.practicum.mainservice.dto.compilation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import java.util.List;

@Validated
@Getter
@Setter
public class UpdateCompilationRequest {

    private List<Long> events;

    private Boolean pinned = false;

    @Size(max = 50, message = "Заголовок не может быть длиннее 50 символов")
    private String title;

}
