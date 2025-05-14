package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private Long shippingAddressId;
    private String email;
    private LocalDate date;
    private String status;
    private Double totalAmount;
    private Set<OrderItemDTO> orderItems;
    private PaymentDTO payment;
}
