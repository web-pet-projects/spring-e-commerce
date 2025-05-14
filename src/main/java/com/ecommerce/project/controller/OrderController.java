package com.ecommerce.project.controller;

import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderRequest;
import com.ecommerce.project.payload.PaymentDTO;
import com.ecommerce.project.payload.PaymentRequest;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.service.StripeService;
import com.ecommerce.project.utils.AuthUtils;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OrderController {
    private final OrderService orderService;
    private final StripeService stripeService;
    private final AuthUtils authUtils;

    @PostMapping("payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable String paymentMethod,
                                               @Valid @RequestBody OrderRequest orderRequest) {
        String email = authUtils.loggedInEmail();
        OrderDTO orderDTO = orderService.placeOrder(email, paymentMethod, orderRequest);

        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);

    }

    @PostMapping("/payments/create-intent")
    public ResponseEntity<String> createIntent(@Valid @RequestBody PaymentRequest paymentRequest) throws StripeException {
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(paymentRequest);
        return new ResponseEntity<>(paymentIntent.getClientSecret(), HttpStatus.CREATED);
    }
}
