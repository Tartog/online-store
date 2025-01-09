package com.example.OnlineStore.repository;

import com.example.OnlineStore.model.Cart;
import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUser(User user);
    Optional<Cart> findByProductAndUser(Product product, User user);
    void deleteAllByUser(User user);
}
