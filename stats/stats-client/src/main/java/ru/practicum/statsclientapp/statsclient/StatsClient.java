package ru.practicum.statsclientapp.statsclient;

import dto.EndpointDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.statsclientapp.baseclient.BaseClient;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        uris.forEach(System.out::println);
        log.info("StatsClient/getStats/ start {}, end {}, List uris size {}, Boolean unique {}", start, end, uris.size(), unique);
        log.info("StatsClient/getStats/ List uris size {}",  uris.size());
        return get("/stats?start={start}&end={end}&\" + uris + \"unique={unique}", parameters);
    }

    public ResponseEntity<Object> create(EndpointDto endpointDto) {
        log.info("StatsClient/hit\" + endpoint" + endpointDto);
        return post("/hit", endpointDto);
    }
}