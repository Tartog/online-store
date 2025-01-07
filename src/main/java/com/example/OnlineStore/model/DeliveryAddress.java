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

    @NotEmpty(message = "Поле 'город' обязательно для заполнения !")
    @Column(name = "city")
    private String city;

    @NotEmpty(message = "Поле 'улица' обязательно для заполнения !")
    @Column(name = "street")
    private String street;

    @NotNull(message = "Поле 'номер дома' обязательно для заполнения !")
    @Min(value = 1, message = "Номер дома не может быть меньше 1 !")
    @Column(name = "house_number")
    private int houseNumber;

    //@OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "delivery_address_id")
    //private Set<Order> orders = new HashSet<Order>();
}
