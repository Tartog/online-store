package com.example.OnlineStore.service;

import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.User;

import java.util.List;

public interface ProductService {
    List<Product> findAllProduct(User seller);
    void saveProduct(Product product);
    Product findById(Long id);
    void updateProduct(Product product);
    void deleteProduct(Long id);
    boolean existsProduct(Product product);
    void deleteProductByName(String name);
}
