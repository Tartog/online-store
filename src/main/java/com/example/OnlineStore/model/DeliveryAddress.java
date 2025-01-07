package com.example.OnlineStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "delivery_address")
@Data
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "City should not be empty !")
    @Column(name = "city")
    private String city;

    @NotEmpty(message = "Street should not be empty !")
    @Column(name = "street")
    private String street;

    @NotNull(message = "House number should not be empty !")
    @Min(value = 1, message = "The house number cannot be less than 1 !")
    @Column(name = "house_number")
    private int houseNumber;

    //@OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "delivery_address_id")
    //private Set<Order> orders = new HashSet<Order>();
}
