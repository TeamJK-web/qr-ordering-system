package com.restaurant.ordering.dto;
import com.restaurant.ordering.model.Payment;
public record PaymentRequest(Long cashierId, Payment.Method method, String referenceNo) {}
