package com.example.OnlineStore.repository;

import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByUser(User seller);
    Optional<Product> findProductByName(String name);
    void deleteProductByName(String name);
}
