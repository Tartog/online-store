package com.example.OnlineStore.service;

import com.example.OnlineStore.model.Order;
import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.ProductsInOrder;
import com.example.OnlineStore.model.User;

import java.util.List;

public interface ProductsInOrderService {
    List<ProductsInOrder> findAllProductsInOrder(Order order);
    void saveProductsInOrder(ProductsInOrder productsInOrder);
    ProductsInOrder findById(Long id);
    void updateProductsInOrder(ProductsInOrder productsInOrder);
    void deleteProductsInOrder(Long id);
    List<ProductsInOrder> findAll();
}
