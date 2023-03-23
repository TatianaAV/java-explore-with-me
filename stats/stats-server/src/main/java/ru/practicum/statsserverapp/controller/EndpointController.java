package ru.practicum.statsserverapp.controller;

import dto.EndpointDto;
import dto.EndpointDtoOutput;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsserverapp.service.EndpointService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class EndpointController {
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EndpointService endpointService;


    @PostMapping("/hit")
    public EndpointDto create(@Valid @RequestBody EndpointDto endpointDto) {
        log.info("EndpointController/hit\" + endpoint" + endpointDto);
        return endpointService.create(endpointDto);
    }

    @GetMapping("/stats")
    public List<EndpointDtoOutput> getStats(@NotBlank @RequestParam String start,
                                            @NotBlank @RequestParam String end,
                                            @NonNull @RequestParam List<String> uris,
                                            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("EndpointController/getStats/ start {}, end {}, List uris size {}, Boolean unique {}", start, end, uris.size(), unique);
        uris.forEach(System.out::println);
        return endpointService.getStats(
                LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), FORMAT),
                LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), FORMAT),
                uris,
                unique);
    }
}