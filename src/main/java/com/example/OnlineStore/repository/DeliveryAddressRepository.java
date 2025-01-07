package com.example.OnlineStore.repository;

import com.example.OnlineStore.model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    DeliveryAddress findDeliveryAddressById(Long id);
    Optional<DeliveryAddress> findDeliveryAddressByCityAndStreetAndHouseNumber(String city, String street, int houseNumber);
}
