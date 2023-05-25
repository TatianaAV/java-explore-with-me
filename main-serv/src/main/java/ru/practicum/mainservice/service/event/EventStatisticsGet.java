package ru.practicum.mainservice.service.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ViewStatsDto;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.model.ParticipationRequest;
import ru.practicum.mainservice.repository.ParticipationRequestRepository;
import ru.practicum.statsclientapp.statsclient.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class EventStatisticsGet {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private EventStatisticsGet() {
        throw new IllegalStateException("Utility class");
    }

    public static void addViewsAndConfirmedRequestsToEvents(
            List<EventFullDto> events,
            Boolean unique,
            StatsClient statsClient,
            ParticipationRequestRepository requestRepository
    ) {
        log.info("addViewsAndConfirmedRequestsToEvents events {}", events);
        log.info("addViewsAndConfirmedRequestsToEvents statsClient {}", statsClient);
        log.info("addViewsAndConfirmedRequestsToEvents requestRepository {}", requestRepository);

        addConfirmedRequests(events, requestRepository);
        addViewsToEvents(events, unique, statsClient);
    }

    public static void addViewsToEvents(List<EventFullDto> events, Boolean unique, StatsClient statsClient) {

        Map<String, EventFullDto> eventsMap = events
                .stream()
                .collect(Collectors.toMap(event -> "/events/" + event.getId(), event -> event));

        log.info("addViewsToEvents eventsMap {}", eventsMap.entrySet());

        Object rawStatistics = statsClient.getStats(
                LocalDateTime.parse("2000-01-01 00:00:00", FORMATTER).format(FORMATTER),
                LocalDateTime.parse("2050-01-01 00:00:00", FORMATTER).format(FORMATTER),
                new ArrayList<>(eventsMap.keySet()),
                unique
        ).getBody();

        assert rawStatistics != null;
        log.info("addViewsToEvents rawStatistics {}", rawStatistics);
        List<ViewStatsDto> statistics = objectMapper.convertValue(rawStatistics, new TypeReference<>() {
        });
        log.info("addViewsToEvents rawStatistics {}", statistics.toString());
        statistics.forEach(statistic -> {
            if (eventsMap.containsKey(statistic.getUri())) {
                eventsMap.get(statistic.getUri()).setViews(statistic.getHits());
            }
        });
        eventsMap.values().stream()
                .filter(event -> event.getViews() == null)
                .forEach(event -> event.setViews(0L));

    }

    public static void addConfirmedRequests(List<EventFullDto> events, ParticipationRequestRepository requestRepository) {
        Map<Long, Integer> requestsCountMap = new HashMap<>();
        log.info("addConfirmedRequests events {}", events);
        log.info("addViewsAndConfirmedRequestsToEvents requestRepository {}", requestRepository);
        List<ParticipationRequest> requests = requestRepository.findAllConfirmedByEventIdIn(events
                .stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList())
        );
        log.info("addViewsAndConfirmedRequestsToEvents requests {}", requests.toString());

        requests.forEach(request -> {
            long eventId = request.getEvent().getId();

            if (!requestsCountMap.containsKey(eventId)) {
                requestsCountMap.put(eventId, 0);
            }

            requestsCountMap.put(eventId, requestsCountMap.get(eventId) + 1);
        });

        events.forEach(event -> {
            if (requestsCountMap.containsKey(event.getId())) {
                event.setConfirmedRequests(requestsCountMap.get(event.getId()));
            }
        });
    }
}

