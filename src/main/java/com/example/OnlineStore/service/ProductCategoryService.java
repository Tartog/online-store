package com.example.OnlineStore.service;

import com.example.OnlineStore.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> findAllProductCategory();
    ProductCategory saveProductCategory(ProductCategory productCategory);
    ProductCategory findById(Long id);
    void updateProductCategory(ProductCategory ProductCategory);
    void deleteProductCategory(Long id);
    void deleteByCategory(String category);
    boolean existsByCategoryAndIdNot(String category, Long id);
    boolean existsProductCategoryByCategory(String category);
    Page<ProductCategory> findAllProductCategory(Pageable pageable);
    long countTotalAddresses();
    Page<ProductCategory> findByCategoryContaining(String category, Pageable pageable);
}
