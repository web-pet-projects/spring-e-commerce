package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;

    @NotBlank(message = "{global.name.blank}")
    @Size(min = 5, message = "{category.name.size}")
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Product> products = new ArrayList<>();
}
