package com.example.OnlineStore.service;

import com.example.OnlineStore.model.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> findAllProductCategory();
    ProductCategory saveProductCategory(ProductCategory productCategory);
    ProductCategory findById(Long id);
    void updateProductCategory(ProductCategory ProductCategory);
    void deleteProductCategory(Long id);
    void deleteByCategory(String category);
    boolean existsCategory(ProductCategory productCategory);
}
