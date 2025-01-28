package com.example.OnlineStore.service;

import com.example.OnlineStore.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<Order> findAllOrder();
    Order saveOrder(Order order);
    Order findById(Long id);
    void updateOrder(Order order);
    void deleteOrder(Long id);
    List<Order> findAllByUser(User user);
    Page<Order> findAllOrders(Pageable pageable);
    Page<Order> findAllOrdersByAddress(DeliveryAddress deliveryAddress, Pageable pageable);
    List<Order> findAllByAddress(DeliveryAddress deliveryAddress);
    Page<Order> findAllOrdersById(Long id, Pageable pageable);
    Page<Order> findAllOrders(User user, Pageable pageable);
}
