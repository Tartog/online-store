package com.example.OnlineStore.service;

import com.example.OnlineStore.model.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressService {
    List<DeliveryAddress> findAllDeliveryAddress();
    DeliveryAddress saveDeliveryAddress(DeliveryAddress deliveryAddress);
    DeliveryAddress findById(Long id);
    boolean existsAddress(DeliveryAddress deliveryAddress);
    void updateDeliveryAddress(DeliveryAddress deliveryAddress);
    void deleteDeliveryAddress(Long id);
}
