package com.example.OnlineStore.repository;

import com.example.OnlineStore.model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    DeliveryAddress findDeliveryAddressById(Long id);
    Optional<DeliveryAddress> findDeliveryAddressByCityAndStreetAndHouseNumber(String city, String street, int houseNumber);

    @Modifying
    @Transactional
    @Query("update DeliveryAddress u set u.city = ?1, u.street = ?2, u.houseNumber = ?3 where u.id = ?4")
    void setDeliveryAddressInfoById(String city, String street, int houseNumber, Long id);
}
