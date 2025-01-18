package com.example.OnlineStore.repository;

import com.example.OnlineStore.model.DeliveryAddress;
import com.example.OnlineStore.model.Order;
import com.example.OnlineStore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByUser(User user);
    List<Order> findAllByDeliveryAddress(DeliveryAddress deliveryAddress);
    Page<Order> findAllByDeliveryAddress(DeliveryAddress deliveryAddress, Pageable pageable);
    Page<Order> findById(Long id, Pageable pageable);

    //@Override
    //Page<Order> findAll(Specification<Order> spec, Pageable pageable);
}
