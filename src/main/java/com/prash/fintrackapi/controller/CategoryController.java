package com.prash.fintrackapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.prash.fintrackapi.dto.CategoryDTO;
import com.prash.fintrackapi.dto.CategoryRequestDTO;
import com.prash.fintrackapi.model.TransactionType;
import com.prash.fintrackapi.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Create a new category
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryRequestDTO request) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(request.getName());
        categoryDTO.setType(request.getType());
        categoryDTO.setDescription(request.getDescription());
        
        CategoryDTO created = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Get categories by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByType(@PathVariable TransactionType type) {
        List<CategoryDTO> categories = categoryService.getCategoriesByType(type);
        return ResponseEntity.ok(categories);
    }

    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    // Update category
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, 
                                                       @Valid @RequestBody CategoryRequestDTO request) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(request.getName());
        categoryDTO.setType(request.getType());
        categoryDTO.setDescription(request.getDescription());
        
        CategoryDTO updated = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(updated);
    }

    // Delete category
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}