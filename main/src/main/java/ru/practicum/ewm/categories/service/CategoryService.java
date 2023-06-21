package ru.practicum.ewm.categories.service;

import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.model.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);
    void deleteCategory(Long catId);
    CategoryDto updateCategory(Long catId, CategoryDto category);
    List<CategoryDto> getAllCategories(Integer from, Integer size);
    CategoryDto getCategoryById(Long catId);
}
