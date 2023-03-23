package ru.practicum.statsclientapp.statscontroller;


import dto.EndpointDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsclientapp.statsclient.StatsClient;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsController {

    private final StatsClient statsClient;


    @PostMapping("/hit")
    public ResponseEntity<Object> create(@Valid @RequestBody EndpointDto endpointDto) {
        log.info("StatsController/hit\" + endpoint" + endpointDto);
        return statsClient.create(endpointDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@NotBlank @RequestParam String start,
                                           @NotBlank @RequestParam String end,
                                           @RequestParam List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("StatsController/getStats/ start {}, end {}, List uris size {}, Boolean unique {}", start, end, uris.size(), unique);
        log.info("StatsController/getStats/ List uris size {}", uris.size());
        return statsClient.getStats(
                start, end, uris, unique);
    }
}