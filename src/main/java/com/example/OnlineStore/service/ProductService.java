package com.example.OnlineStore.service;

import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<Product> findAllProduct(User seller);
    void saveProduct(Product product);
    Product findById(Long id);
    void updateProduct(Product product);
    void deleteProduct(Long id);
    boolean existsProduct(Product product);
    void deleteProductByName(String name);
    List<Product> findAll();
    Page<Product> findAllProductsBySeller(User user, Pageable pageable);
    List<Product> findByNameContaining(String name);
}
