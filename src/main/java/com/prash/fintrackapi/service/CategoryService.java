package com.prash.fintrackapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prash.fintrackapi.dto.CategoryDTO;
import com.prash.fintrackapi.model.Category;
import com.prash.fintrackapi.model.TransactionType;
import com.prash.fintrackapi.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Create a new category
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new RuntimeException("Category with name '" + categoryDTO.getName() + "' already exists");
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        category.setDescription(categoryDTO.getDescription());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        Category saved = categoryRepository.save(category);
        return mapToDTO(saved);
    }

    // Get all categories
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get categories by type
    public List<CategoryDTO> getCategoriesByType(TransactionType type) {
        return categoryRepository.findByType(type).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get category by ID
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return mapToDTO(category);
    }

    // Update category
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Check if new name already exists (excluding current category)
        if (!category.getName().equals(categoryDTO.getName())
                && categoryRepository.existsByName(categoryDTO.getName())) {
            throw new RuntimeException("Category with name '" + categoryDTO.getName() + "' already exists");
        }

        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        category.setDescription(categoryDTO.getDescription());
        category.setUpdatedAt(LocalDateTime.now());

        Category updated = categoryRepository.save(category);
        return mapToDTO(updated);
    }

    // Delete category
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // Helper method to map Category to CategoryDTO
    private CategoryDTO mapToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setType(category.getType());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}