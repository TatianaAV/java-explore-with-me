package ru.practicum.statsserverapp.service;

import dto.EndpointDto;
import dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointService {
    EndpointDto create(EndpointDto endpoint);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
