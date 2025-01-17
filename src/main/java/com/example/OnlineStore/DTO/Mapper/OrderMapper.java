package com.example.OnlineStore.DTO.Mapper;

import com.example.OnlineStore.DTO.OrderDTO;
import com.example.OnlineStore.model.Order;

public class OrderMapper {
    public static OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setOrderDate(order.getOrderDate());
        dto.setExpectedReceiveDate(order.getExpectedReceiveDate());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setUser(UserMapper.toDTO(order.getUser()));
        dto.setProductsInOrders(ProductsInOrderMapper.toDTOSet(order.getProductsInOrders()));
        return dto;
    }
}
