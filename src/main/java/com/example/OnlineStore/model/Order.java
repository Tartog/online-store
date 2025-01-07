package com.example.OnlineStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Order date should not be empty !")
    @Column(name = "order_date")
    private Date orderDate;

    /*@OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Set<ItemsInCart> itemsInCarts = new HashSet<ItemsInCart>();*/

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private DeliveryAddress deliveryAddress;
}
