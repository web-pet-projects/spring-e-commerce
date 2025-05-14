package com.ecommerce.project.service;

import com.ecommerce.project.payload.PaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface StripeService {
    PaymentIntent createPaymentIntent(PaymentRequest paymentRequest) throws StripeException;
}
