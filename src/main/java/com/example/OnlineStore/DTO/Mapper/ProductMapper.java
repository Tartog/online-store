package com.example.OnlineStore.DTO.Mapper;

import com.example.OnlineStore.DTO.ProductDTO;
import com.example.OnlineStore.model.Product;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductMapper {
    //private static final Set<Product> processedProducts = new HashSet<>();

    public static ProductDTO toDTO(Product product) {
        /*if (product == null || processedProducts.contains(product)) {
            return null;
        }
        processedProducts.add(product);*/

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setNumberOfProducts(product.getNumberOfProducts());
        dto.setPrice(product.getPrice());
        //dto.setProductCategories(ProductCategoryMapper.toDTOSet(product.getProductCategories()));
        dto.setUser(UserMapper.toDTO(product.getUser()));
        dto.setImagePath(product.getImagePath());
        return dto;
    }

    public static Set<ProductDTO> toDTOSet(Set<Product> products) {
        /*if (products == null) {
            return Set.of();
        }*/
        return products.stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toSet());
    }
}
