package com.example.OnlineStore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products_in_order ")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductsInOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Поле 'количество товара' обязательно для заполнения !")
    @Column(name = "number_of_product")
    private int numberOfProduct;

    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    //@JsonBackReference
    //@JsonIgnore // Игнорируем это поле при сериализации
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
