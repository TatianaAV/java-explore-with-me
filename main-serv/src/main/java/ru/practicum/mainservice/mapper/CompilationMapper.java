package ru.practicum.mainservice.mapper;

import org.springframework.data.domain.Page;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.model.Compilation;
import ru.practicum.mainservice.model.CompilationTitle;
import ru.practicum.mainservice.repository.ParticipationRequestRepository;

import java.util.List;
import java.util.Map;


public interface CompilationMapper {

    List<CompilationDto> toCompilationDto(Map<Long, List<Compilation>> compilationSaved, ParticipationRequestRepository participationRequestRepository);

    List<CompilationDto> toCompilationDtoTitle(List<CompilationTitle> compilationTitleList);

    List<CompilationTitle> toCompilationDtoTitle(Page<CompilationTitle> compilationTitleList);

    CompilationDto toCompilationTitleDto(CompilationTitle compilationTitle);
}
