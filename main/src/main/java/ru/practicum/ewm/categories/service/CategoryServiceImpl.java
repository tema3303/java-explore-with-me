package ru.practicum.ewm.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.model.CategoryMapper;
import ru.practicum.ewm.categories.model.dto.CategoryDto;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.error.exceptions.CategoryNameDoubleException;
import ru.practicum.ewm.error.exceptions.NotFoundException;
import ru.practicum.ewm.error.exceptions.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new CategoryNameDoubleException("имя уже занято");
        }
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        checkCategoryId(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto category) {
        Category oldCategory = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category", catId));
        if (categoryRepository.existsByName(category.getName()) && !category.getName().equals(oldCategory.getName())) {
            throw new CategoryNameDoubleException("имя уже занято");
        }
        checkCategoryId(catId);
        Category updateCategory = CategoryMapper.toCategory(getCategoryById(catId));
        updateCategory.setName(category.getName());
        return CategoryMapper.toCategoryDto(updateCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        List<Category> categories;
        if (from < 0 || size <= 0) {
            throw new ValidationException("Значения не могут быть отрицательными");
        }
        Pageable pagination = PageRequest.of(from / size, size);
        categories = categoryRepository.findAll(pagination).getContent();
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category", catId)));
    }

    private void checkCategoryId(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category", catId);
        }
    }
}