package ru.practicum.mainservice.service.comment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ViewStatsDto;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.mainservice.dto.comment.CommentFullDto;
import ru.practicum.statsclientapp.statsclient.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CommentStatisticsGet {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private CommentStatisticsGet() {
        throw new IllegalStateException("Utility class");
    }

    public static void addViewsToComments(List<CommentFullDto> comments, StatsClient statsClient) {

        Map<String, CommentFullDto> commentMap = comments
                .stream()
                .collect(Collectors.toMap(comment -> "/comment/" + comment.getId(), comment -> comment));

        log.info("addViewsToComment commentMap {}", commentMap.entrySet());

        Object rawStatistics = statsClient.getStats(
                LocalDateTime.parse("2000-01-01 00:00:00", FORMATTER).format(FORMATTER),
                LocalDateTime.parse("2050-01-01 00:00:00", FORMATTER).format(FORMATTER),
                new ArrayList<>(commentMap.keySet()),
                false
        ).getBody();

        assert rawStatistics != null;
        log.info("addViewsToComment rawStatistics {}", rawStatistics);
        List<ViewStatsDto> statistics = objectMapper.convertValue(rawStatistics, new TypeReference<>() {
        });
        log.info("addViewsToComment rawStatistics {}", statistics.toString());
        statistics.forEach(statistic -> {
            if (commentMap.containsKey(statistic.getUri())) {
                commentMap.get(statistic.getUri()).setViews(statistic.getHits());
            }
        });
    }
}

