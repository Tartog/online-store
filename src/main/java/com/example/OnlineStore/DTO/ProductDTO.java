package com.example.OnlineStore.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private int numberOfProducts;
    private double price;
    private Set<ProductCategoryDTO> productCategories;
    private UserDTO user;
    private String imagePath;
}
