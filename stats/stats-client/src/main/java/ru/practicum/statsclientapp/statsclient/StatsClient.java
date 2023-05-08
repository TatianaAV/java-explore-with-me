package ru.practicum.statsclientapp.statsclient;

import dto.EndpointDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class StatsClient {

    private final WebClient webClient;

    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        webClient = WebClient.builder()
                .baseUrl(serverUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void addEndpointHit(EndpointDto endpointDto) {
        webClient.post()
                .uri("/hit")
                .body(Mono.just(endpointDto), EndpointDto.class)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.CREATED)) {
                        return response.bodyToMono(Object.class)
                                .map(body -> ResponseEntity.status(HttpStatus.CREATED).body(body));
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();
    }

    public ResponseEntity<Object> getStats(
            String start,
            String end,
            List<String> uris,
            Boolean unique
    ) {
        String paramsUri = uris.stream().reduce("", (result, uri) -> result + "&uris=" + uri);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .query(paramsUri)
                        .queryParam("unique", unique)
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(Object.class)
                                .map(body -> ResponseEntity.ok().body(body));
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();
    }

/*    private final WebClient webClient;

    public EndpointDto addEndpointHit(EndpointDto endpointHitDto) {

        return webClient
                .post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(endpointHitDto), EndpointDto.class)
                .retrieve()
                .bodyToMono(EndpointDto.class)
                .block();
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
*//* UriTemplate template = UriTemplate.fromTemplate(
                "/users{?ids*}&from={from}&size={size}");
        template.set("ids", ids);
        template.set("from", from);
        template.set("size", size);
        String expanded = template.expand();*//*
       return Arrays.asList(Objects.requireNonNull(webClient
               .get()
               .uri(uriBuilder -> uriBuilder
                       .path("/stats{?uris*}&start={start}&end={end}&unique={unique}")
                       .queryParam("start", start)
                       .queryParam("end", end)
                       .queryParam("uris", uris)
                       .queryParam("unique", unique)
                       .build())
               .retrieve()
               .bodyToMono(ViewStatsDto[].class)
               .block()));
    }*/
}