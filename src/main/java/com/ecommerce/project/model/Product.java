package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank(message = "{global.name.blank}")
    @Size(min = 3, max = 50, message="{product.name.size}")
    private String productName;
    private String description;
    private String image;

    @PositiveOrZero
    private Double discount;

    @PositiveOrZero
    private Integer quantity;

    @PositiveOrZero
    private Double price;

    @PositiveOrZero
    private Double specialPrice;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User seller;

    @PrePersist
    @PreUpdate
    public void calculateSpecialPrice() {
        this.specialPrice = this.price * (1 - this.discount * 0.01);
    }

}
