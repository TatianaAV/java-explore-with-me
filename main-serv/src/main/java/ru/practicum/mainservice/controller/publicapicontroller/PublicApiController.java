package ru.practicum.mainservice.controller.publicapicontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.event.EntenteParams;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.service.category.CategoryService;
import ru.practicum.mainservice.service.compilation.CompilationService;
import ru.practicum.mainservice.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicApiController {

    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("GET PublicApiController categories/from {}, size {}", from, size);
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("GET PublicApiController categories/catId {}", catId);
        return categoryService.getCategoryById(catId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilation(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                               @RequestParam(name = "pinned", defaultValue = "false") String pinned) {
        log.info("GET PublicApiController getCompilations/from {}, size {}, pinned {}", from, size, pinned);

        return compilationService.getCompilation(PageRequest.of(from / size, size), Boolean.valueOf(pinned));
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("GET PublicApiController/ getCompilationById/{}", compId);
        return compilationService.getCompilationById(compId);
    }

    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventFullById(@PathVariable Long eventId,
                                         HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        log.info("GET PublicApiController/ getEventFullById/ eventId {}", eventId);
        return eventService.getEventById(eventId, request);
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsWithFilter(@RequestParam(name = "text", required = false) String text,
                                             @RequestParam(name = "categories", required = false) Long categories,
                                             @RequestParam(name = "paid", required = false) Boolean paid,
                                             @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                             @RequestParam(name = "sort", required = false) String sort,
                                             @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                             HttpServletRequest request) {
        log.info("GET PublicApiController/ getEventsWithFilter");
        EntenteParams ententeParams;
        ententeParams = new EntenteParams(
                text,
                categories,
                paid,
                EntenteParams.ofStart(rangeStart),
                EntenteParams.ofEnd(rangeEnd),
                sort,
                onlyAvailable,
                from,
                size);
        log.info("text: {}", text);
        log.info("categories: {}", categories);
        log.info("paid: {}", paid);
        log.info("rangeStart: {}", rangeStart);
        log.info("rangeEnd: {}", rangeEnd);
        log.info("sort: {}", sort);
        log.info("onlyAvailable: {}", onlyAvailable);
        log.info("from: {}", from);
        log.info("size: {}", size);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}, {}, {}",request.getProtocol(), request.getHttpServletMapping(), request.getRequestURI());

        return eventService.getEventsWithFilter(ententeParams, request);
    }
}