package com.example.OnlineStore.DTO;

import lombok.Data;

@Data
public class CartDTO {
    private Long id;
    private UserDTO user;
    private ProductDTO product;
    private int numberOfProduct;
}
