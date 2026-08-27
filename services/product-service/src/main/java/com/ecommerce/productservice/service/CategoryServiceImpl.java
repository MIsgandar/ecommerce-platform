package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.CategoryResponse;
import com.ecommerce.productservice.dto.CreateCategoryRequest;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.exception.CategoryAlreadyExistsException;
import com.ecommerce.productservice.exception.CategoryInUseException;
import com.ecommerce.productservice.exception.CategoryNotFoundException;
import com.ecommerce.productservice.repository.CategoryRepo;
import com.ecommerce.productservice.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{


    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {

        if(categoryRepo.existsByName(request.name())) {
            throw new CategoryAlreadyExistsException(
                    "Category already exists: " + request.name()
            );
        }

        Category category = Category.builder()
                .name(request.name())
                .description(request.description())
                .build();

        Category savedCategory = categoryRepo.save(category);

        return mapToResponse(savedCategory);
    }

    @Override
    public CategoryResponse getCategory(UUID id) {

        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + id
                        )
                );

        return mapToResponse(category);

    }

     @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public CategoryResponse updateCategory(UUID id, CreateCategoryRequest request) {

        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " +id
                ));

        if(categoryRepo.existsByNameAndIdNot(request.name(), id)) {
            throw new CategoryAlreadyExistsException(
                    "Category already exists: " + request.name()
            );
        }

        category.setName(request.name());
        category.setDescription(request.description());

        return mapToResponse(category);
    }

    @Override
    public void deleteCategory(UUID id) {

        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + id
                ));

        if (productRepo.existsByCategoryId(id)) {
            throw new CategoryInUseException(
                    "Category cannot be deleted because it is used by products: " + id
            );
        }

        categoryRepo.delete(category);

    }

    private CategoryResponse mapToResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
                );

    }
}
