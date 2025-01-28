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

    @Size(min = 2, max = 30, message = "Название товара должно состоять из 2-30 символов !")
    @NotEmpty(message = "Поле 'название товара' обязательно для заполнения !")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Поле 'количество товара' обязательно для заполнения !")
    @Column(name = "number_of_products")
    @Min(value = 0, message = "Количество товара не может быть меньше 0 !")
    private int numberOfProducts;

    @NotNull(message = "Поле 'цена' обязательно для заполнения !")
    @Column(name = "price")
    @Min(value = 1, message = "Цена не может быть меньше 1 !")
    private double price;

    @NotNull(message = "Товар должен иметь хотя бы 1 категорию !")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_in_category",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "product_category_id", referencedColumnName="id")
    )
    private Set<ProductCategory> productCategories;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @Column(name = "image_path")
    private String imagePath;

    public String getProductCategoriesAsString() {
        return productCategories.stream()
                .map(ProductCategory::getCategory)
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
                .append(", imagePath='").append(imagePath).append("'")
                .append(", categoriesCount=").append(productCategories != null ? productCategories.size() : 0)
                .append('}');
        return sb.toString();
    }
}
