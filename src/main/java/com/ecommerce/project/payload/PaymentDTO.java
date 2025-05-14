package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long id;
    private String method;
    private String pgPaymentId;
    private String pgPaymentStatus;
    private String pgPaymentResponse;
    private String pgName;
    private Long orderId;
}
