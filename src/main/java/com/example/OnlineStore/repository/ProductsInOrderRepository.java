package com.example.OnlineStore.repository;

import com.example.OnlineStore.model.Order;
import com.example.OnlineStore.model.ProductsInOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsInOrderRepository extends JpaRepository<ProductsInOrder, Long> {
    List<ProductsInOrder> findProductsInOrderByOrder(Order order);
}
