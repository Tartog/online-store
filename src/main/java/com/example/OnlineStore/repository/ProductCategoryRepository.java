package com.example.OnlineStore.repository;

import com.example.OnlineStore.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    ProductCategory findProductCategoryById(Long id);
    Optional<ProductCategory> findProductCategoryByCategory(String category);
    void deleteByCategory(String category);
}
