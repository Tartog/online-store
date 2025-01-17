package com.example.OnlineStore.DTO.Mapper;

import com.example.OnlineStore.DTO.ProductCategoryDTO;
import com.example.OnlineStore.model.ProductCategory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductCategoryMapper {
    //private static final Set<ProductCategory> processedCategories = new HashSet<>();

    public static ProductCategoryDTO toDTO(ProductCategory productCategory) {
        /*if (productCategory == null || processedCategories.contains(productCategory)) {
            return null;
        }
        processedCategories.add(productCategory);*/

        ProductCategoryDTO dto = new ProductCategoryDTO();
        dto.setId(productCategory.getId());
        dto.setCategory(productCategory.getCategory());
        //dto.setProducts(ProductMapper.toDTOSet(productCategory.getProducts()));
        return dto;
    }

    public static Set<ProductCategoryDTO> toDTOSet(Set<ProductCategory> products) {
        /*if (products == null) {
            return Set.of();
        }*/
        return products.stream()
                .map(ProductCategoryMapper::toDTO)
                .collect(Collectors.toSet());
    }
}
