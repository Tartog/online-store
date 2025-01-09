package com.example.OnlineStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products_in_order ")
@Data
public class ProductsInOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Поле 'количество товара' обязательно для заполнения !")
    @Column(name = "number_of_product")
    private int numberOfProduct;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
