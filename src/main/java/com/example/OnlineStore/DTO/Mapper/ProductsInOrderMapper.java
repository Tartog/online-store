package com.example.OnlineStore.DTO.Mapper;

import com.example.OnlineStore.DTO.ProductsInOrderDTO;
import com.example.OnlineStore.model.ProductsInOrder;

import java.util.Set;
import java.util.stream.Collectors;

public class ProductsInOrderMapper {
    public static ProductsInOrderDTO toDTO(ProductsInOrder productsInOrder) {
        ProductsInOrderDTO dto = new ProductsInOrderDTO();
        dto.setId(productsInOrder.getId());
        dto.setNumberOfProduct(productsInOrder.getNumberOfProduct());
        dto.setProduct(ProductMapper.toDTO_FULL(productsInOrder.getProduct()));
        return dto;
    }

    public static Set<ProductsInOrderDTO> toDTOSet(Set<ProductsInOrder> productsInOrder) {
        return productsInOrder.stream()
                .map(ProductsInOrderMapper::toDTO)
                .collect(Collectors.toSet());
    }
}
