package com.example.OnlineStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Поле 'дата доставки' обязательно для заполнения' !")
    @Column(name = "order_date")
    private Date orderDate;

    @NotNull(message = "Поле 'ожидаемая дата получения' обязательно для заполнения' !")
    @Column(name = "expected_receive_date")
    private Date expectedReceiveDate;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private DeliveryAddress deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Set<ProductsInOrder> productsInOrders = new HashSet<ProductsInOrder>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
