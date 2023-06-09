package ru.practicum.mainservice.mapper;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.model.Compilation;
import ru.practicum.mainservice.model.CompilationTitle;
import ru.practicum.mainservice.repository.ParticipationRequestRepository;
import ru.practicum.mainservice.service.event.EventStatisticsGet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CompilationMapperImpl implements CompilationMapper {

    private final EventMapper eventMapper;


    public List<CompilationDto> toCompilationDto(Map<Long, List<Compilation>> compilationSaved, ParticipationRequestRepository participationRequestRepository) {
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Map.Entry<Long, List<Compilation>> entry : compilationSaved.entrySet()) {
            CompilationDto compilationDto = new CompilationDto();
            Long id = entry.getKey();
            List<Compilation> compilationList = entry.getValue();
            Boolean pinned = compilationList.get(0).getTitle().getPined();
            String title = compilationList.get(0).getTitle().getTitle();
            List<EventFullDto> events = compilationList.stream().map(
                            compilation -> eventMapper.toEventFullDto(compilation.getEvent()))
                    .collect(Collectors.toList());

            EventStatisticsGet.addConfirmedRequests(events, participationRequestRepository);

            compilationDto.setId(id);
            compilationDto.setPinned(pinned);
            compilationDto.setTitle(title);
            compilationDto.setEvents(eventMapper.toShortEventFullList(events));
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
