package ru.practicum.statsserverapp.service;


import dto.EndpointDto;
import dto.EndpointDtoOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statsserverapp.handler.NotFoundException;
import ru.practicum.statsserverapp.mapper.EndpointMapper;
import ru.practicum.statsserverapp.model.App;
import ru.practicum.statsserverapp.model.Endpoint;
import ru.practicum.statsserverapp.repo.AppRepository;
import ru.practicum.statsserverapp.repo.EndpointRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EndpointServiceImpl implements EndpointService {
    private final EndpointRepository repository;
    private final AppRepository appRepository;
    private final EndpointMapper mapper;


    @Transactional
    @Override
    public EndpointDto create(EndpointDto endpointDto) {
        log.info("EndpointDto {}", endpointDto.toString());
        String nameApp = endpointDto.getApp();
        Optional<App> appName = appRepository.findByName(nameApp);
        App app;
        if (appName.isEmpty()) {
            app = appRepository.save(new App(nameApp));
        } else {
            app = appName.orElseThrow(() -> new NotFoundException(" rfrf"));
        }
        Endpoint endpoint = mapper.toEndpoint(endpointDto, app);
        return mapper.toEndpointDto(repository.save(endpoint));
    }

    @Override
    public List<EndpointDtoOutput> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<EndpointDtoOutput> stats;
        if (uris.isEmpty()) {
            if (unique.equals(true)) {
                stats = repository.findDistinctByTimestampBetween(start, end);
            } else {
                stats = repository.findAllByTimestampBetween(start, end);
            }
        } else {
            if (unique.equals(true)) {
                stats = repository.findDistinctByTimestampBetweenAndUriIn(start, end, uris);
            } else {
                stats = repository.findAllByTimestampBetweenAndUriIn(start, end, uris);
            }
        }
        log.info("stats.size {}, unique {}, uris.size {}", stats.size(), unique, uris.size());
        return stats;
    }
}
