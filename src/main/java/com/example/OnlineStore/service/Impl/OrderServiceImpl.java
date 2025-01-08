package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.Order;
import com.example.OnlineStore.repository.OrderRepository;
import com.example.OnlineStore.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private OrderRepository repository;

    @Override
    public List<Order> findAllOrder() {
        return repository.findAll();
    }

    @Override
    public Order saveOrder(Order order) {
        return repository.save(order);
    }

    @Override
    public Order findById(Long id) {
        return repository.findById(id).orElseThrow(()->new RuntimeException("Данный заказ отсутствует !"));
    }

    @Override
    public void updateOrder(Order order) {
        repository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        repository.deleteById(id);
    }
}
