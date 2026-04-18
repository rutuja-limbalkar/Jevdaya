package com.jevdaya.service;

import com.jevdaya.Entity.Donation;
import java.util.List;

public interface DonationService {
    Donation saveDonation(Donation donation);
    List<Donation> getAllDonations();
    String createOrder(double amount);   // Moved from PaymentService
    
    byte[] generateReceiptPdf(Donation donation);
}