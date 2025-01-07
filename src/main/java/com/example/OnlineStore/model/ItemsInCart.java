package com.example.OnlineStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "items_in_cart")
@Data
public class ItemsInCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Number of product should not be empty !")
    @Column(name = "number_of_product")
    private int numberOfProduct;
}
