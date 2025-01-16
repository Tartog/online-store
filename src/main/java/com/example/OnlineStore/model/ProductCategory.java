package com.example.OnlineStore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="product_category")
@Data
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Поле 'категория' обязательно для заполнения !")
    @Column(unique = true, name = "category")
    private String category;

    //@JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProductCategory{")
                .append("id=").append(id)
                .append(", category='").append(category).append("'")
                .append(", productsCount=").append(products != null ? products.size() : 0)
                .append('}');
        return sb.toString();
    }
}