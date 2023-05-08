package ru.practicum.mainservice.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mainservice.dto.event.EntenteParams;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.UpdateEventRequest;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.service.category.CategoryService;
import ru.practicum.mainservice.service.compilation.CompilationService;
import ru.practicum.mainservice.service.event.EventService;
import ru.practicum.mainservice.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest createUser) {
        log.info("POST AdminController/users/createUser {}", createUser);
        return userService.createUser(createUser);
    }


    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        EntenteParams ententeParams = new EntenteParams(ids, from, size);
        log.info("GET AdminController/users/getUsers ids.size {}, from {}, size {}", ids, from, size);
        log.info("ids {}", ids);
        return userService.getUsers(ententeParams);
    }

    @DeleteMapping("users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        log.info("DEL AdminController/users/deleteUserById ({})", userId);
        userService.deleteUserById(userId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("POST AdminController/ admin/categories {}", categoryDto);

        return categoryService.createCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable Long catId) {
        log.info("DEL AdminController/ admin/categories/catId {}", catId);
        categoryService.deleteCategoryById(catId);
    }


    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody NewCategoryDto updateCategory) {
        log.info("PATCH AdminController/ admin/categories/{}, updateCategory {}", catId, updateCategory);
        return categoryService.updateCategory(catId, updateCategory);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.info("POST AdminController/ admin/users {}", compilationDto);
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable Long compId) {
        log.info("DEL AdminController/ admin/compilations/ {}", compId);
        compilationService.deleteCompilationById(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@Valid @RequestBody UpdateCompilationRequest updateCompilation,
                                            @PathVariable Long compId) {
        log.info("PATCH AdminController/compilations/{}, updateCompilation{}", compId, updateCompilation);
        return compilationService.updateCompilation(compId, updateCompilation);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@RequestBody UpdateEventRequest updateEventAdminRequest,
                                    @PathVariable Long eventId) {
        log.info("PATCH AdminController/ admin/events/{}, updateEvent{}", eventId, updateEventAdminRequest);
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequest);
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsWithFilter(@RequestParam(name = "ids", required = false) List<Long> ids,
                                                  @RequestParam(name = "states", required = false) String states,
                                                  @RequestParam(name = "categories", required = false) Long categories,
                                                  @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                  @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                  HttpServletRequest request) {
        log.info("GET AdminApiController/ getEventsWithFilter");
        EntenteParams ententeParams = new EntenteParams(
                ids,
                states,
                categories,
                EntenteParams.ofStart(rangeStart),
                EntenteParams.ofEnd(rangeEnd),
                from,
                size);
        log.info("ids: {}", ids);
        log.info("states: {}", states);
        log.info("categories: {}", categories);
        log.info("rangeStart: {}", rangeStart);
        log.info("rangeEnd: {}", rangeEnd);
        log.info("from: {}", from);
        log.info("size: {}", size);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());

        return eventService.getEventsByAdminWithFilter(ententeParams);
    }
}