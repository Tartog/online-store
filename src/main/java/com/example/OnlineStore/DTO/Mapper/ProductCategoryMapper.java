package com.example.OnlineStore.DTO.Mapper;

import com.example.OnlineStore.DTO.ProductCategoryDTO;
import com.example.OnlineStore.model.ProductCategory;

import java.util.Set;
import java.util.stream.Collectors;

public class ProductCategoryMapper {
    public static ProductCategoryDTO toDTO_WithoutProduct(ProductCategory productCategory) {
        ProductCategoryDTO dto = new ProductCategoryDTO();
        dto.setId(productCategory.getId());
        dto.setCategory(productCategory.getCategory());
        return dto;
    }

    public static ProductCategoryDTO toDTO_FULL(ProductCategory productCategory) {
        ProductCategoryDTO dto = new ProductCategoryDTO();
        dto.setId(productCategory.getId());
        dto.setCategory(productCategory.getCategory());
        dto.setProducts(ProductMapper.toDTOSet(productCategory.getProducts()));
        return dto;
    }

    public static Set<ProductCategoryDTO> toDTOSet(Set<ProductCategory> products) {
        return products.stream()
                .map(ProductCategoryMapper::toDTO_WithoutProduct)
                .collect(Collectors.toSet());
    }
}
