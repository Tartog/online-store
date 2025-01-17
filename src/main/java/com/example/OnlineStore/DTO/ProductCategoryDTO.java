package com.example.OnlineStore.DTO;

import lombok.Data;
import java.util.Set;

@Data
public class ProductCategoryDTO {
    private Long id;
    private String category;
    private Set<ProductDTO> products;
}
