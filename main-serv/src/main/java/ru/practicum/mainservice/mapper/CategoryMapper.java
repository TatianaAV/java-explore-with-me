package ru.practicum.mainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toCategoryDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category toCategory(NewCategoryDto user);

    List<CategoryDto> toCategoryDtoList(Page<Category> pageCat);
}
