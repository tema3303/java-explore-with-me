package ru.practicum.ewm.categories.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.categories.model.dto.CategoryDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(CategoryDto category) {
        return Category.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}