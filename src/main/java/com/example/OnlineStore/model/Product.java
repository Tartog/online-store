package com.example.OnlineStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "product")
@Data
@Setter
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 30, message = "Product name should be between 2 and 30 characters !")
    @NotEmpty(message = "Product name should not be empty !")
    //@Column(unique = true, name = "name")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Number of products should not be empty !")
    @Column(name = "number_of_products")
    @Min(value = 0, message = "The number of products cannot be less than 0 !")
    private int numberOfProducts;

    @NotNull(message = "Price should not be empty !")
    @Column(name = "price")
    @Min(value = 1, message = "The price cannot be less than 1 !")
    private double price;

    @NotNull(message = "The product must have at least one category!")
    //@ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_in_category",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "product_category_id", referencedColumnName="id")
    )
    private Set<ProductCategory> productCategories;

    //@ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    public String getProductCategoriesAsString() {
        return productCategories.stream()
                .map(ProductCategory::getCategory) // или используйте другое поле/метод для получения строки
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product{")
                .append("id=").append(id)
                .append(", name='").append(name).append("'")
                .append(", numberOfProducts=").append(numberOfProducts)
                .append(", price=").append(price)
                .append(", categoriesCount=").append(productCategories != null ? productCategories.size() : 0)
                .append('}');
        return sb.toString();
    }
}
