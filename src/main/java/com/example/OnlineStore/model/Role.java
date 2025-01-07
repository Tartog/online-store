package com.example.OnlineStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true, name = "user_role")
    private String userRole;

    //@OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "role_id")
    //private Set<User> users = new HashSet<User>();

    /*public User getUserRole(Long id){

        Optional<User> optionalUser = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();

        User user = optionalUser.orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

        return null;

        //return users.
    }*/
}
