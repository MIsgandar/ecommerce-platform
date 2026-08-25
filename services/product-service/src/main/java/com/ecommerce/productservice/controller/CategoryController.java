package com.ecommerce.productservice.controller;


import com.ecommerce.productservice.dto.CategoryResponse;
import com.ecommerce.productservice.dto.CreateCategoryRequest;
import com.ecommerce.productservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody @Valid
            CreateCategoryRequest request) {

        CategoryResponse response = categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {

        CategoryResponse response = categoryService.getCategory(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        return ResponseEntity.ok(categoryService.getAllCategories());

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @RequestBody CreateCategoryRequest request
    )
    {

        return ResponseEntity.ok(categoryService.updateCategory(id, request));


    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable UUID id
    ) {

        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();

    }



}
