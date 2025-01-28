package com.example.OnlineStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 0, max = 30, message = "Имя должно состоять не более чем из 30 символов !")
    @Column(name = "first_name")
    private String firstName;

    @Size(min = 0, max = 30, message = "Фамилия должна состоять не более чем из 30 символов !")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "Поле 'пароль' обязательно для заполнения !")
    @Column(name = "password_hash")
    private String passwordHash;

    @Size(min = 4, max = 30, message = "Логин должен состоять из 4-30 символов !")
    @NotEmpty(message = "Поле 'логин' обязательно для заполнения !")
    @Column(unique = true, name = "login")
    private String login;

    @NotEmpty(message = "Поле 'почта' обязательно для заполнения !")
    @Column(unique = true, name = "email")
    @Email(message = "Почта введена некорректно !")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    private Set<Product> products = new HashSet<Product>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) &&
                Objects.equals(login, user.login) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, email);
    }
}