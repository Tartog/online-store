package com.example.OnlineStore.DTO;

import com.example.OnlineStore.model.DeliveryAddress;
import com.example.OnlineStore.model.OrderStatus;
import lombok.Data;

import java.sql.Date;
import java.util.Set;

@Data
public class OrderDTO {
    private Long id;
    private Date orderDate;
    private Date expectedReceiveDate;
    private OrderStatus orderStatus;
    private DeliveryAddress deliveryAddress;
    private UserDTO user;
    private Set<ProductsInOrderDTO> productsInOrders;
}
