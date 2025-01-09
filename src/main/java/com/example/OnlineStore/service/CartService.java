package com.example.OnlineStore.service;

import com.example.OnlineStore.model.Cart;
import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.User;

import java.util.List;

public interface CartService {
    List<Cart> findAllCart();
    void saveCart(Cart cart);
    Cart findById(Long id);
    void updateCart(Cart cart);
    void deleteCart(Long id);
    List<Cart> findAllByUser(User user);
    boolean productExist(Product product, User user);
    Cart findByUserAndProduct(Product product, User user);
    void deleteAllByUser(User user);
}
