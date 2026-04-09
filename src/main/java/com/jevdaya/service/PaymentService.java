package com.jevdaya.service;

import com.jevdaya.Entity.Payment;

public interface PaymentService {
    String createOrder(double amount);
    String verifyPayment(String orderId, String paymentId, String signature,Double amount,
    		String customerName, String customerEmail,Long customerContact);
}