package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.DeliveryAddress;
import com.example.OnlineStore.model.Order;
import com.example.OnlineStore.model.ProductCategory;
import com.example.OnlineStore.model.User;
import com.example.OnlineStore.repository.OrderRepository;
import com.example.OnlineStore.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public Page<Order> findAllOrders(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Order> findAllOrdersByAddress(DeliveryAddress deliveryAddress, Pageable pageable) {
        return repository.findAllByDeliveryAddress(deliveryAddress, pageable);
    }

    @Override
    public Page<Order> findAllOrdersById(Long id, Pageable pageable) {
        return repository.findById(id, pageable);
    }

    @Override
    public Page<Order> findAllOrders(User user, Pageable pageable) {
        return repository.findAllByUser(user, pageable);
    }

    @Override
    public List<Order> findAllByAddress(DeliveryAddress deliveryAddress) {
        return repository.findAllByDeliveryAddress(deliveryAddress);
    }

    /*@Override
    public Page<Order> findByFilters(String city, String street, Integer houseNumber, String status, Long id, Pageable pageable) {
        Specification<Order> spec = Specification.where(null);

        if (city != null && !city.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("city"), "%" + city + "%"));
        }
        if (street != null && !street.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("street"), "%" + street + "%"));
        }
        if (houseNumber != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("houseNumber"), houseNumber));
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("status"), "%" + status + "%"));
        }
        if (id != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
        }

        return repository.findAll(spec, pageable);
    }*/
}
