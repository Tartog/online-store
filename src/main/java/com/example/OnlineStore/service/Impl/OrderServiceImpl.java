package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.Order;
import com.example.OnlineStore.model.User;
import com.example.OnlineStore.repository.OrderRepository;
import com.example.OnlineStore.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
@Transactional
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

    @Override
    public List<Order> findAllByUser(User user) {
        return repository.findAllByUser(user);
    }
}
