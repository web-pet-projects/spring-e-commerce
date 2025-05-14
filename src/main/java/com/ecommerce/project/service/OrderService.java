package com.ecommerce.project.service;

import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderRequest;

public interface OrderService {
    OrderDTO placeOrder(String email, String paymentMethod, OrderRequest orderRequest);
}
