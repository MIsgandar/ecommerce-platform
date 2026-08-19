package com.ecommerce.productservice.service;


import com.ecommerce.productservice.dto.CreateProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.dto.UpdateProductRequest;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.entity.ProductStatus;
import com.ecommerce.productservice.exception.CategoryNotFoundException;
import com.ecommerce.productservice.exception.ProductAlreadyExistsException;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.repository.CategoryRepo;
import com.ecommerce.productservice.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {

        if(productRepo.existsBySku(request.sku())) {
            throw new ProductAlreadyExistsException(
                    "product with SKU already exists: " + request.sku()
            );
        }
            Category category = categoryRepo.findById(request.categoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(
                            "Category not found: " + request.categoryId()
                    ));

            Product product = Product.builder()
                    .sku(request.sku())
                    .name(request.name())
                    .description(request.description())
                    .price(request.price())
                    .quantity(request.quantity())
                    .status(request.productStatus())
                    .category(category)
                    .build();

            Product savedProduct = productRepo.save(product);

            return mapToResponse(savedProduct);
        }

        @Override
        public ProductResponse getProduct(UUID id) {

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with ID: " + id
                ));

        return mapToResponse(product);
        }

    @Override
    public Page<ProductResponse> getAllProducts(ProductStatus productStatus,
                                                UUID categoryId,
                                                Pageable pageable) {

        Page<Product> products;

        if(productStatus == null && categoryId == null) {
            products = productRepo.findAll(pageable);
        }
        else if(productStatus != null && categoryId == null) {
            products = productRepo.findByStatus(productStatus, pageable);
        } else if (productStatus == null && categoryId != null) {
            products = productRepo.findByCategoryId(categoryId, pageable);
        } else {
            products = productRepo.findByStatusAndCategoryId(
                    productStatus,categoryId,pageable);
        }
        return products.map(this::mapToResponse);
    }

    @Override
    public ProductResponse updateProduct(UUID id, UpdateProductRequest request) {


        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found: " + id
                ));
        Category category = categoryRepo.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found: " + request.categoryId()
                ));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(category);

        return mapToResponse(product);
    }

    @Override
    public void deleteProduct(UUID id) {

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found: " + id
                ));

        productRepo.delete(product);
    }

    private ProductResponse mapToResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getStatus(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
