package com.jevdaya.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Data
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Donor Personal Details with Validation + Unique Constraints
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name must contain only letters and spaces")
    private String name;

     @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

   
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be exactly 10 digits")
    private String mobile;

  
    @Column(name = "adhar_card") // Increase length for encryption
    private String adharCard;

    @Column(name = "pan_number")
    private String panNumber;
    
    @NotBlank(message = "Address is required")
    private String address;
    // Donation Details
    @NotNull(message = "Donation type is required")
    private Long productId;

    @NotNull(message = "Donation amount is required")
    @DecimalMin(value = "10.0", message = "Minimum donation amount is ₹10")
    private Double donationAmount;

    // Payment Details (Razorpay)
    private String orderId;
    private String paymentId;
 
    @Column(name = "receipt_generated", nullable = false)
    private boolean receiptGenerated = false;
    @NotBlank(message = "Status is required")
    private String status;   // SUCCESS, FAILED, PENDING

    private LocalDateTime paymentDate;

    // Additional fields
    private boolean isOffline = false;
    private String paymentMode = "RAZORPAY";
    private String referenceNumber;
    private String notes;
    
    
}