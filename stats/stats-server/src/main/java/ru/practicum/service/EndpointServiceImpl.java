package ru.practicum.service;


import dto.EndpointDto;
import dto.EndpointDtoOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.EndpointMapper;
import ru.practicum.model.Endpoint;
import ru.practicum.repo.EndpointRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EndpointServiceImpl implements EndpointService {
    private final EndpointRepository repository;
    private final EndpointMapper mapper;


    @Transactional
    @Override
    public EndpointDto create(EndpointDto endpointDto) {
        log.info("EndpointDto {}", endpointDto.toString());
        Endpoint endpoint = mapper.toEndpoint(endpointDto);
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
                log.info("stats.size {}", stats.size());
            }
        }
        return stats;
    }
}