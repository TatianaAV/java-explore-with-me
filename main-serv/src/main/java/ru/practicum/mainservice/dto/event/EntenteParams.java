package ru.practicum.mainservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EntenteParams {

    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private List<Long> ids;
    private String text;
    private Long categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private String sort;
    private Boolean onlyAvailable;
    private Integer from;
    private Integer size;

    public EntenteParams(String text, Long categories, Boolean paid,
                         LocalDateTime rangeStart,
                         LocalDateTime rangeEnd,
                         String sort, Boolean onlyAvailable, Integer from, Integer size) {
        this.text = text;
        this.categories = categories;
        this.paid = paid;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.sort = sort;
        this.onlyAvailable = onlyAvailable;
        this.from = from;
        this.size = size;
    }

    public EntenteParams(List<Long> ids, Integer from, Integer size) {
        this.ids = ids;
        this.from = from;
        this.size = size;
    }

    public EntenteParams(List<Long> ids, String states, Long categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        this.ids = ids;
        this.text = states;
        this.categories = categories;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.from = from;
        this.size = size;
    }


    public static LocalDateTime ofStart(String rangeStart) {
        if (rangeStart == null) {
            return null;
        }
        return LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8), FORMAT);
    }

    public static LocalDateTime ofEnd(String rangeEnd) {
        if (rangeEnd == null) {
            return null;
        }
        return LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8), FORMAT);
    }
}
