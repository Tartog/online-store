package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.OrderStatus;
import com.example.OnlineStore.repository.OrderStatusRepository;
import com.example.OnlineStore.service.OrderStatusService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Primary
@Transactional
public class OrderStatusServiceImpl implements OrderStatusService, CommandLineRunner {

    private OrderStatusRepository repository;

    @Override
    public List<OrderStatus> findAllOrderStatus() {
        return repository.findAll();
    }

    @Override
    public OrderStatus saveOrderStatus(OrderStatus orderStatus) {
        return repository.save(orderStatus);
    }

    @Override
    public OrderStatus findById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void updateOrderStatus(OrderStatus orderStatus) {
        repository.save(orderStatus);
    }

    @Override
    public void deleteOrderStatus(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<OrderStatus> findByStatus(String status) {
        return repository.findByStatus(status);
    }

    @Override
    public void run(String... args) throws Exception {
        OrderStatus delivered = new OrderStatus();
        delivered.setStatus("Delivered");
        OrderStatus awaitingReceipt = new OrderStatus();
        awaitingReceipt.setStatus("Awaiting receipt");
        OrderStatus received = new OrderStatus();
        received.setStatus("Received");
        if (repository.findByStatus(delivered.getStatus()).isEmpty()){
            repository.save(delivered);
        }
        if (repository.findByStatus(awaitingReceipt.getStatus()).isEmpty()){
            repository.save(awaitingReceipt);
        }
        if (repository.findByStatus(received.getStatus()).isEmpty()){
            repository.save(received);
        }
    }
}
