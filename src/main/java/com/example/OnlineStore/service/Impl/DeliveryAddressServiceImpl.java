package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.DeliveryAddress;
import com.example.OnlineStore.repository.DeliveryAddressRepository;
import com.example.OnlineStore.service.DeliveryAddressService;
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
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    private DeliveryAddressRepository repository;

    @Override
    public List<DeliveryAddress> findAllDeliveryAddress() {
        return repository.findAll();
    }

    @Override
    public DeliveryAddress saveDeliveryAddress(DeliveryAddress deliveryAddress) {
        return repository.save(deliveryAddress);
    }

    @Override
    public DeliveryAddress findById(Long id) {
        return repository.findDeliveryAddressById(id);
    }

    @Override
    public boolean existsAddress(DeliveryAddress deliveryAddress) {
        return repository.findDeliveryAddressByCityAndStreetAndHouseNumber(
                deliveryAddress.getCity(),
                deliveryAddress.getStreet(),
                deliveryAddress.getHouseNumber()).isPresent();
    }

    @Override
    public void updateDeliveryAddress(DeliveryAddress deliveryAddress) {
        repository.setDeliveryAddressInfoById(
                deliveryAddress.getCity(),
                deliveryAddress.getStreet(),
                deliveryAddress.getHouseNumber(),
                deliveryAddress.getId());
    }

    @Override
    public void deleteDeliveryAddress(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<DeliveryAddress> findAllDeliveryAddress(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public long countTotalAddresses() {
        return repository.count();
    }

    @Override
    public Page<DeliveryAddress> findByFilters(String city, String street, Integer houseNumber, Pageable pageable) {
        Specification<DeliveryAddress> spec = Specification.where(null);

        if (city != null && !city.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("city"), "%" + city + "%"));
        }
        if (street != null && !street.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("street"), "%" + street + "%"));
        }
        if (houseNumber != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("houseNumber"), houseNumber));
        }

        return repository.findAll(spec, pageable);
    }

    @Override
    public List<DeliveryAddress> findByFilters(String city, String street, Integer houseNumber) {
        Specification<DeliveryAddress> spec = Specification.where(null);

        if (city != null && !city.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("city"), "%" + city + "%"));
        }
        if (street != null && !street.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("street"), "%" + street + "%"));
        }
        if (houseNumber != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("houseNumber"), houseNumber));
        }

        return repository.findAll(spec);
    }
}
