package com.example.OnlineStore.DTO.Mapper;

import com.example.OnlineStore.DTO.CartDTO;
import com.example.OnlineStore.model.Cart;

public class CartMapper {
    public static CartDTO toDTO(Cart cart){
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUser(UserMapper.toDTO(cart.getUser()));
        dto.setProduct(ProductMapper.toDTO_FULL(cart.getProduct()));
        dto.setNumberOfProduct(cart.getNumberOfProduct());
        return dto;
    }
}
