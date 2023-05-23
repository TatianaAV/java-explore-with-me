package ru.practicum.mainservice.service.category;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.handler.NotValidatedExceptionConflict;
import ru.practicum.mainservice.handler.NotFoundException;
import ru.practicum.mainservice.mapper.CategoryMapper;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.repository.CategoryRepository;
import ru.practicum.mainservice.repository.EventRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        log.info("POST AdminController/ admin/users {}", categoryDto);
        Category newCategory = categoryMapper.toCategory(categoryDto);
        return categoryMapper.toCategoryDto(categoryRepository.save(newCategory));
    }

    @Override
    public void deleteCategoryById(Long catId) {
        log.info("DEL AdminController/ admin/catId/{}", catId);
        List<Event> events = eventRepository.findEventsByCategory_Id(catId);
        if (events.isEmpty()) {
            categoryRepository.deleteById(catId);
        } else {
            throw new NotValidatedExceptionConflict("Категория используется, удаление запрещено");
        }
    }

    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto updateCategory) {
        log.info("PATCH AdminController/ admin/categories/{}, {}", catId, updateCategory);
        Category findCategory = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Not found category"));

        findCategory.setName(updateCategory.getName());
        return categoryMapper.toCategoryDto(findCategory);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Page<Category> pageCat = categoryRepository.findAll(PageRequest.of(from / size, size));
        return categoryMapper.toCategoryDtoList(pageCat);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategoryById(Long catId) {
        log.info("GET AdminController/ admin/categories/catId {}", catId);
        Category findCategory = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория не найдена"));
        return categoryMapper.toCategoryDto(findCategory);
    }
}

