package com.jevdaya.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String paymentId;
    private String signature;
    private Double amount;
    private String status;
    
    private String customerName;
    private String customerEmail;
    private Long customerContact;
}