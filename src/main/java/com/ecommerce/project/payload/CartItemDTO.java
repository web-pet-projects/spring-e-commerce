package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    private Long id;
    private ProductDTO product;
    private Long cartId;
    private Integer quantity;
    private Double discount;
    private Double price;
}
