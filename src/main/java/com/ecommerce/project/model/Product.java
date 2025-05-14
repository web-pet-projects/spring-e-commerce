package com.ecommerce.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank(message = "{global.name.blank}")
    @Size(min = 3, message="{product.name.size}")
    @NonNull
    private String productName;
    private String description;
    private String image;

    @PositiveOrZero
    @NonNull
    private Double discount;

    @PositiveOrZero
    @NonNull
    private Integer quantity;

    @PositiveOrZero
    @NonNull
    private Double price;

    @PositiveOrZero
    private Double specialPrice;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "category_id")
    @NonNull
    private Category category;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<OrderItem> orderItems = new HashSet<>();

    @PrePersist
    @PreUpdate
    public void calculateSpecialPrice() {
        this.specialPrice = this.price * (1 - this.discount * 0.01);
    }

}
