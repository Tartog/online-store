package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.ProductCategory;
import com.example.OnlineStore.repository.ProductCategoryRepository;
import com.example.OnlineStore.service.ProductCategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private ProductCategoryRepository repository;

    @Override
    public List<ProductCategory> findAllProductCategory() {
        return repository.findAll();
    }

    @Override
    public ProductCategory saveProductCategory(ProductCategory productCategory) {
        return repository.save(productCategory);
    }

    @Override
    public ProductCategory findById(Long id) {
        return repository.findProductCategoryById(id);
    }

    @Override
    public void updateProductCategory(ProductCategory productCategory) {
        repository.setCategoryInfoById(
                productCategory.getCategory(),
                productCategory.getId());
    }

    @Override
    public void deleteProductCategory(Long id) {
        ProductCategory category = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        // Удаляем связь с товарами
        for (Product product : category.getProducts()) {
            product.getProductCategories().remove(category);
        }

        // Удаляем категорию
        //repository.delete(category);
        repository.deleteById(id);
    }

    @Override
    public void deleteByCategory(String category) {
        ProductCategory productCategory = repository.findProductCategoryByCategory(category)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        for (Product product : productCategory.getProducts()) {
            product.getProductCategories().remove(productCategory);
        }
        repository.deleteByCategory(category);
    }

    @Override
    public boolean existsByCategoryAndIdNot(String category, Long id) {
        return repository.existsByCategoryAndIdNot(category, id);
    }

    @Override
    public boolean existsProductCategoryByCategory(String category) {
        return repository.existsProductCategoryByCategory(category);
    }

    @Override
    public Page<ProductCategory> findAllProductCategory(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public long countTotalAddresses() {
        return repository.count();
    }

    @Override
    public Page<ProductCategory> findByCategoryContaining(String category, Pageable pageable) {
        return repository.findByCategoryContaining(category, pageable);
    }
}
