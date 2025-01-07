package com.example.OnlineStore.service;

import com.example.OnlineStore.model.Cart;

import java.util.List;

public interface CartService {
    List<Cart> findAllCart();
    void saveCart(Cart cart);
    Cart findById(Long id);
    void updateCart(Cart cart);
    void deleteCart(Long id);
}
