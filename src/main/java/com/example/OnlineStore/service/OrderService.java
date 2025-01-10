package com.example.OnlineStore.service;

import com.example.OnlineStore.model.DeliveryAddress;
import com.example.OnlineStore.model.Order;
import com.example.OnlineStore.model.OrderStatus;
import com.example.OnlineStore.model.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAllOrder();
    Order saveOrder(Order order);
    Order findById(Long id);
    void updateOrder(Order order);
    void deleteOrder(Long id);
    List<Order> findAllByUser(User user);
    List<Order> findAllByAddress(DeliveryAddress deliveryAddress);
}
