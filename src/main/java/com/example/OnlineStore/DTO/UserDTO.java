package com.example.OnlineStore.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String login;
    private String email;
    private String phoneNumber;
    private String role;
    private Set<ProductDTO> products;
}
