package com.example.OnlineStore.service;

import com.example.OnlineStore.model.DeliveryAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeliveryAddressService {
    List<DeliveryAddress> findAllDeliveryAddress();
    DeliveryAddress saveDeliveryAddress(DeliveryAddress deliveryAddress);
    DeliveryAddress findById(Long id);
    boolean existsAddress(DeliveryAddress deliveryAddress);
    void updateDeliveryAddress(DeliveryAddress deliveryAddress);
    void deleteDeliveryAddress(Long id);
    Page<DeliveryAddress> findAllDeliveryAddress(Pageable pageable);
    long countTotalAddresses();
    Page<DeliveryAddress> findByFilters(String city, String street, Integer houseNumber, Pageable pageable);
    List<DeliveryAddress> findByFilters(String city, String street, Integer houseNumber);
}
