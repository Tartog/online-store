package com.example.OnlineStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "order_status")
@Data
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Status should not be empty !")
    @Column(name = "status")
    private String status;

    /*@OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_status_id")
    private Set<Order> orders = new HashSet<Order>();*/
}
