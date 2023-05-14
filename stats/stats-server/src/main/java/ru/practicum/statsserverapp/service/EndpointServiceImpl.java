package ru.practicum.statsserverapp.service;


import dto.EndpointDto;
import dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.statsserverapp.handler.EndpointEmptyAppException;
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
        if (endpointDto.getApp() == null || endpointDto.getApp().isEmpty()) {
            throw new EndpointEmptyAppException("App is empty.");
        }

        if (endpointDto.getUri() == null || endpointDto.getUri().isEmpty()) {
            throw new EndpointEmptyAppException("Uri is empty.");
        }

        String nameApp = endpointDto.getApp();
        Optional<App> appName = appRepository.findByName(nameApp);
        App app;
        if (appName.isEmpty()) {
            app = appRepository.save(new App(nameApp));
        } else {
            app = appName.orElseThrow(() -> new NotFoundException("App error"));
        }
        Endpoint endpoint = mapper.toEndpoint(endpointDto, app);
        return mapper.toEndpointDto(
                repository.save(endpoint));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStatsDto> stats;
        if (uris == null || uris.isEmpty()) {
            log.info("uris null equals {}",   uris);
            if (unique.equals(true)) {
                log.info("findDistinctByTimestampBetween {}", unique);
                stats = repository.findDistinctByTimestampBetween(start, end, PageRequest.of(0, 1));
            } else {
                log.info("findAllByTimestampBetween, unique {}", unique);
                stats = repository.findAllByTimestampBetween(start, end);
            }
        } else {
            log.info("uris not null {}",   uris);
            if (unique.equals(true)) {
                log.info("findDistinctByTimestampBetweenAndUriIn {}", unique);
                stats = repository.findDistinctByTimestampBetweenAndUriIn(start, end, uris, PageRequest.of(0, 1));
            } else {
                log.info("findAllByTimestampBetweenAndUriIn {}", unique);
                stats = repository.findAllByTimestampBetweenAndUriIn(start, end, uris);
            }
        }
        log.info("stats.size {}, unique {}, uris {}", stats.size(), unique, uris);
        return stats;
    }
}
