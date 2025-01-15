package com.example.OnlineStore.repository;

import com.example.OnlineStore.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    ProductCategory findProductCategoryById(Long id);
    Optional<ProductCategory> findProductCategoryByCategory(String category);
    void deleteByCategory(String category);
    boolean existsByCategoryAndIdNot(String category, Long id);
    boolean existsProductCategoryByCategory(String category);

    @Modifying
    @Transactional
    @Query("update ProductCategory u set u.category = ?1 where u.id = ?2")
    void setCategoryInfoById(String category, Long id);
}
