package com.example.OnlineStore.service;

import com.example.OnlineStore.model.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderStatusService {
    List<OrderStatus> findAllOrderStatus();
    OrderStatus saveOrderStatus(OrderStatus orderStatus);
    OrderStatus findById(Long id);
    void updateOrderStatus(OrderStatus orderStatus);
    void deleteOrderStatus(Long id);
    Optional<OrderStatus> findByStatus(String status);
}
