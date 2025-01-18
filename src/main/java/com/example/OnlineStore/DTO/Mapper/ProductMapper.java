package com.example.OnlineStore.DTO.Mapper;

import com.example.OnlineStore.DTO.ProductDTO;
import com.example.OnlineStore.model.Product;

import java.util.Set;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductDTO toDTO_WithoutCategory(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setNumberOfProducts(product.getNumberOfProducts());
        dto.setPrice(product.getPrice());
        dto.setUser(UserMapper.toDTO_WithoutProducts(product.getUser()));
        dto.setImagePath(product.getImagePath());
        return dto;
    }

    public static ProductDTO toDTO_FULL(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setNumberOfProducts(product.getNumberOfProducts());
        dto.setPrice(product.getPrice());
        dto.setProductCategories(ProductCategoryMapper.toDTOSet(product.getProductCategories()));
        dto.setUser(UserMapper.toDTO_WithoutProducts(product.getUser()));
        dto.setImagePath(product.getImagePath());
        return dto;
    }


    public static Set<ProductDTO> toDTOSet(Set<Product> products) {
        return products.stream()
                .map(ProductMapper::toDTO_WithoutCategory)
                .collect(Collectors.toSet());
    }
}
