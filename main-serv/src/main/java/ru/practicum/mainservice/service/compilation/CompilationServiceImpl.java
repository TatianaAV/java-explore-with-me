package ru.practicum.mainservice.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mainservice.handler.NotFoundException;
import ru.practicum.mainservice.mapper.CompilationMapper;
import ru.practicum.mainservice.model.Compilation;
import ru.practicum.mainservice.model.CompilationTitle;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.repository.CompilationRepository;
import ru.practicum.mainservice.repository.CompilationTitleRepository;
import ru.practicum.mainservice.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;

    private final CompilationRepository compilationRepository;
    private final CompilationTitleRepository compilationTitleRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {

        List<Event> events = eventRepository.findByIdIn(compilationDto.getEvents());
        List<Compilation> compilations = new ArrayList<>();
        String nameCompilation = compilationDto.getTitle();

        Optional<CompilationTitle> title = compilationTitleRepository.findByTitle(nameCompilation);

        CompilationTitle newTitle;

        if (title.isEmpty() && events.isEmpty()) {
            newTitle = compilationTitleRepository.save(new CompilationTitle(compilationDto.getTitle(), compilationDto.getPinned()));
            return new CompilationDto(newTitle.getId(), newTitle.getPined(), newTitle.getTitle());
        }
        if (title.isEmpty()) {
            newTitle = compilationTitleRepository.save(new CompilationTitle(compilationDto.getTitle(), compilationDto.getPinned()));

        } else {
            newTitle = title.orElseThrow(() -> new NotFoundException("CompilationTitle error"));
        }
        if (!events.isEmpty() && newTitle != null) {
            for (Event event : events) {
                compilations.add(new Compilation(null, newTitle, event));
            }
        }
        Map<Long, List<Compilation>> compilationSaved = compilationRepository.saveAll(compilations).stream().collect(groupingBy(compilation -> compilation.getTitle().getId(), toList()));

        List<CompilationDto> compilation = compilationMapper.toCompilationDto(compilationSaved);
//add views
        return compilation.get(0);
    }

    @Override
    public void deleteCompilationById(Long compId) {
        compilationRepository.deleteAllByTitle_Id(compId);
        compilationTitleRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilation) {
        CompilationTitle title = compilationTitleRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        List<Compilation> list = compilationRepository.findAllByTitle_Id(compId);

        if (updateCompilation.getTitle() != null) {
            title.setTitle(updateCompilation.getTitle());
        }
        if (updateCompilation.getPinned() != null) {
            title.setPined(updateCompilation.getPinned());
        }
        if (list.isEmpty() && !updateCompilation.getEvents().isEmpty()) {

            List<Event> events = eventRepository.findByIdIn(updateCompilation.getEvents());
            List<Compilation> compilations = new ArrayList<>();

            for (Event event : events) {
                compilations.add(new Compilation(null, title, event));
            }

            Map<Long, List<Compilation>> compilationSaved = compilationRepository.saveAll(compilations).stream().collect(groupingBy(compilation -> compilation.getTitle().getId(), toList()));

            List<CompilationDto> compilation = compilationMapper.toCompilationDto(compilationSaved);
//add views
            return compilation.get(0);
        }
        if (!list.isEmpty() && !updateCompilation.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findByIdIn(updateCompilation.getEvents());
            List<Compilation> compilations = new ArrayList<>();
            compilationRepository.deleteAllByTitle_Id(compId);
            for (Event event : events) {
                compilations.add(new Compilation(null, title, event));
            }

            Map<Long, List<Compilation>> newCompilationSaved = compilationRepository.saveAll(compilations).stream().collect(groupingBy(compilation -> compilation.getTitle().getId(), toList()));

            List<CompilationDto> compilation = compilationMapper.toCompilationDto(newCompilationSaved);
//add views
            return compilation.get(0);
        }

        Map<Long, List<Compilation>> newCompilation = list.stream().collect(groupingBy(compilation -> compilation.getTitle().getId(), toList()));

        List<CompilationDto> compilation = compilationMapper.toCompilationDto(newCompilation);
//add views
        return compilation.get(0);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getCompilation(PageRequest page, Boolean pinned) {

        Page<CompilationTitle> compilationTitleList = compilationTitleRepository.findByPined(pinned, page);
        if (compilationTitleList.isEmpty()) {
            return List.of();
        }
        List<CompilationTitle> compilationTitle = compilationMapper.toCompilationDtoTitle(compilationTitleList);
        List<CompilationDto> compilationsDtos = compilationMapper.toCompilationDtoTitle(compilationTitle);

        Map<Long, List<Compilation>> compilationMap = compilationRepository.findAllByTitleIn(compilationTitle)
                .stream()
                .collect(groupingBy(compilation -> compilation.getTitle().getId(), toList()));
        if (compilationMap.values().isEmpty()) {
            return compilationsDtos;
        }

        List<CompilationDto> compilations = compilationMapper.toCompilationDto(compilationMap);
        if (compilations.size() == compilationsDtos.size()) {
            return compilations;
        }

        for (CompilationDto compilationDto1 : compilations) {
            for (CompilationDto compilationDto : compilationsDtos) {
                if (compilationDto1.getId().equals(compilationDto.getId())) {
                    compilationDto.setEvents(compilationDto1.getEvents());
                }
            }
        }
        return compilationsDtos;
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilationById(Long compId) {
        List<Compilation> list = compilationRepository.findAllByTitle_Id(compId);
        if (list.isEmpty()) {
            CompilationTitle title = compilationTitleRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена"));
            return new CompilationDto(title.getId(), title.getPined(), title.getTitle());
        }
        Map<Long, List<Compilation>> compilationMap = list.stream().collect(groupingBy(compilation -> compilation.getTitle().getId(), toList()));
        List<CompilationDto> compilation = compilationMapper.toCompilationDto(compilationMap);
        return compilation.get(0);
    }
}
