package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.ProductCategory;
import com.example.OnlineStore.repository.ProductCategoryRepository;
import com.example.OnlineStore.service.ProductCategoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
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
        repository.save(productCategory);
    }

    @Override
    public void deleteProductCategory(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByCategory(String category) {
        repository.deleteByCategory(category);
    }

    @Override
    public boolean existsCategory(ProductCategory productCategory) {
        return repository.findProductCategoryByCategory(productCategory.getCategory()).isPresent();
    }
}
