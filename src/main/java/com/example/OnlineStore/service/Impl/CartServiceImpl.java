package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.Cart;
import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.User;
import com.example.OnlineStore.repository.CartRepository;
import com.example.OnlineStore.service.CartService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
@Transactional
public class CartServiceImpl implements CartService {

    private CartRepository repository;

    @Override
    public List<Cart> findAllCart() {
        return repository.findAll();
    }

    @Override
    public void saveCart(Cart cart) {
        repository.save(cart);
    }

    @Override
    public Cart findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Данная корзина отсутствует !"));
    }

    @Override
    public void updateCart(Cart cart) {
        repository.save(cart);
    }

    @Override
    public void deleteCart(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Cart> findAllByUser(User user) {
        return repository.findAllByUser(user);
    }

    @Override
    public boolean productExist(Product product, User user) {
        return repository.findByProductAndUser(product, user).isEmpty();
    }

    @Override
    public Cart findByUserAndProduct(Product product, User user) {
        return repository.findByProductAndUser(product, user).orElseThrow(()->
                new RuntimeException("Данный товар или пользователь отсутствует"));
    }
}
