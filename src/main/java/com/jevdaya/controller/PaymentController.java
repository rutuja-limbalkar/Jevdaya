package com.jevdaya.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jevdaya.service.PaymentService;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/create-order")
    public String createOrder(@RequestParam double amount) {
        return service.createOrder(amount);
    }

    @PostMapping("/verify")
    public String verifyPayment(@RequestBody Map<String, String> data) {
    	 String amountStr = data.get("amount");
         Double amount = amountStr != null ? Double.parseDouble(amountStr) : null;
         
         
         String customerName = data.get("customerName");
         String customerEmail = data.get("customerEmail");
         Long customerContact=null;
         String contactStr = data.get("customerContact");
         
         if (contactStr != null && !contactStr.trim().isEmpty()) {
             try {
                 customerContact = Long.parseLong(contactStr.trim());
             } catch (NumberFormatException e) {
                 // Log warning if needed, but continue with null
                 System.err.println("Invalid contact number format: " + contactStr);
             }
         }

         return service.verifyPayment(
                 data.get("orderId"),
                 data.get("paymentId"),
                 data.get("signature"),
                 amount,
                 customerName,
                 customerEmail,
                 customerContact
        );
    }
}