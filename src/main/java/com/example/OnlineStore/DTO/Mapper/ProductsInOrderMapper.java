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
        dto.setProduct(ProductMapper.toDTO(productsInOrder.getProduct()));
        //dto.setOrder(OrderMapper.toDTO(productsInOrder.getOrder()));

        return dto;
    }

    public static Set<ProductsInOrderDTO> toDTOSet(Set<ProductsInOrder> productsInOrder) {
        /*if (productsInOrder == null) {
            return Set.of();
        }*/
        return productsInOrder.stream()
                .map(ProductsInOrderMapper::toDTO)
                .collect(Collectors.toSet());
    }
}
