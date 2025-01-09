package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.Order;
import com.example.OnlineStore.model.ProductsInOrder;
import com.example.OnlineStore.repository.ProductsInOrderRepository;
import com.example.OnlineStore.service.ProductsInOrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
@Transactional
public class ProductsInOrderServiceImpl implements ProductsInOrderService {

    private ProductsInOrderRepository repository;

    @Override
    public List<ProductsInOrder> findAllProductsInOrder(Order order) {
        return repository.findProductsInOrderByOrder(order);
    }

    @Override
    public void saveProductsInOrder(ProductsInOrder productsInOrder) {
        repository.save(productsInOrder);
    }

    @Override
    public ProductsInOrder findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Данных товаров в заказе не существует !"));
    }

    @Override
    public void updateProductsInOrder(ProductsInOrder productsInOrder) {
        repository.save(productsInOrder);
    }

    @Override
    public void deleteProductsInOrder(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<ProductsInOrder> findAll() {
        return repository.findAll();
    }
}
