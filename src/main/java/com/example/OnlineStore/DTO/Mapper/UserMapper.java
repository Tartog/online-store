package com.example.OnlineStore.DTO.Mapper;

import com.example.OnlineStore.DTO.UserDTO;
import com.example.OnlineStore.model.User;

public class UserMapper {
    public static UserDTO toDTO(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPasswordHash(user.getPasswordHash());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole().getUserRole());
        dto.setProducts(ProductMapper.toDTOSet(user.getProducts()));
        return dto;
    }

    public static UserDTO toDTO_WithoutProducts(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPasswordHash(user.getPasswordHash());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole().getUserRole());
        return dto;
    }
}
