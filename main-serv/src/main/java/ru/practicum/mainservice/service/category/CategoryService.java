package ru.practicum.mainservice.service.category;

import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto categoryDto);

    void deleteCategoryById(Long catId);

    CategoryDto updateCategory(Long catId, NewCategoryDto updateCategory);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);
}
