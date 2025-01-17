package com.example.OnlineStore.DTO;

import lombok.Data;

@Data
public class ProductsInOrderDTO {
    private Long id;
    private int numberOfProduct;
    private ProductDTO product;
    private OrderDTO order;
}
