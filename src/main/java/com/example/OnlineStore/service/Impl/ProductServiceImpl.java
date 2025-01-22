package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.ProductCategory;
import com.example.OnlineStore.model.User;
import com.example.OnlineStore.repository.ProductCategoryRepository;
import com.example.OnlineStore.repository.ProductRepository;
import com.example.OnlineStore.service.ProductService;
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
public class ProductServiceImpl implements ProductService {

    private ProductRepository repository;
    private ProductCategoryRepository repositoryCategory;

    @Override
    public List<Product> findAllProduct(User seller) {
        return repository.findAllByUser(seller);
    }

    @Override
    public void saveProduct(Product product) {
        repository.save(product);
    }

    @Override
    public Product findById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void updateProduct(Product product) {
        repository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Удаляем связи с категориями
        for (ProductCategory category : product.getProductCategories()) {
            category.getProducts().remove(product);
        }

        // Удаляем товар
        repository.delete(product);
    }

    @Override
    public boolean existsProduct(Product product) {
        return repository.findProductByName(product.getName()).isPresent();
    }

    @Override
    public void deleteProductByName(String name) {
        repository.deleteProductByName(name);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Product> findAllProductsBySeller(User seller, Pageable pageable) {
        return repository.findAllProductsByUser(seller, pageable);
    }
}
