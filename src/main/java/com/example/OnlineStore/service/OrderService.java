package com.example.OnlineStore.service;

import com.example.OnlineStore.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAllOrder();
    Order saveOrder(Order order);
    Order findById(Long id);
    void updateOrder(Order order);
    void deleteOrder(Long id);
}
