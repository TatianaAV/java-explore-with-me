package ru.practicum.mainservice.mapper;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.model.Compilation;
import ru.practicum.mainservice.model.CompilationTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CompilationMapperImpl implements CompilationMapper {

    public List<CompilationDto> toCompilationDto(Map<Long, List<Compilation>> compilationSaved) {
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Map.Entry<Long, List<Compilation>> entry : compilationSaved.entrySet()) {
            CompilationDto compilationDto = new CompilationDto();
            Long id = entry.getKey();
            List<Compilation> compilationList = entry.getValue();
            Boolean pinned = compilationList.get(0).getTitle().getPined();
            String title = compilationList.get(0).getTitle().getTitle();
            List<EventShortDto> events = compilationList.stream()
                    .map(comp -> new EventShortDto(
                            comp.getEvent().getId(),
                            comp.getEvent().getTitle(),
                            comp.getEvent().getAnnotation(),
                            new CategoryDto(comp.getEvent().getCategory().getId(), comp.getEvent().getCategory().getName()),
                            comp.getEvent().getConfirmedRequests(),
                            comp.getEvent().getEventDate(),
                            new UserShortDto(comp.getEvent().getInitiator().getId(), comp.getEvent().getInitiator().getName()),
                            comp.getEvent().getPaid()))
                    .collect(Collectors.toList());

            compilationDto.setId(id);
            compilationDto.setPinned(pinned);
            compilationDto.setTitle(title);
            compilationDto.setEvents(events);
            compilationDtoList.add(compilationDto);
        }
        return compilationDtoList;
    }

    @Override
    public List<CompilationDto> toCompilationDtoTitle(List<CompilationTitle> compilationTitleList) {
        if (compilationTitleList == null) {
            return null;
        }

        List<CompilationDto> list = new ArrayList<CompilationDto>(compilationTitleList.size());
        for (CompilationTitle compilationTitle : compilationTitleList) {
            list.add(toCompilationTitleDto(compilationTitle));
        }

        return list;
    }

    @Override
    public List<CompilationTitle> toCompilationDtoTitle(Page<CompilationTitle> page) {
        if (page == null) {
            return null;
        }

        List<CompilationTitle> list = new ArrayList<CompilationTitle>();
        for (CompilationTitle compilation : page) {
            list.add(compilation);
        }

        return list;
    }

    @Override
    public CompilationDto toCompilationTitleDto(CompilationTitle compilationTitle) {
        return new CompilationDto(compilationTitle.getId(), compilationTitle.getPined(), compilationTitle.getTitle());
    }
}
