package com.example.OnlineStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="product_category")
@Data

public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Category should not be empty !")
    @Column(unique = true, name = "category")
    private String category;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_in_category",
            joinColumns = @JoinColumn(name = "product_category_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName="id")
    )
    private Set<Product> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategory that = (ProductCategory) o;
        return Objects.equals(id, that.id); // Сравниваем по id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Генерируем hashCode на основе id
    }
}