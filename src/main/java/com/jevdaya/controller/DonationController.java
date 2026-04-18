package com.jevdaya.controller;

import com.jevdaya.Entity.Donation;
import com.jevdaya.repo.DonationRepository;
import com.jevdaya.service.DonationService;
import com.jevday.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private DonationRepository donationRepository;

    @GetMapping("/donations")
    public List<Donation> getAllDonations() {
        return donationService.getAllDonations();
    }

    @GetMapping("/my-profile")
    public Map<String, Object> getMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (email == null || email.equals("anonymousUser")) {
            throw new RuntimeException("User not authenticated");
        }

        List<Donation> donations = donationRepository.findByEmail(email);

        Donation latest = donations.stream()
                .max(Comparator.comparing(Donation::getPaymentDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .orElse(null);

        Map<String, Object> profile = new HashMap<>();

        if (latest != null) {
            profile.put("name", latest.getName() != null ? latest.getName() : "");
            profile.put("email", latest.getEmail());
            profile.put("mobile", latest.getMobile());
            
            // DECRYPTING FOR DISPLAY
            profile.put("adharCard", (latest.getAdharCard() != null && !latest.getAdharCard().isEmpty()) 
                    ? AESUtil.decrypt(latest.getAdharCard()) : "");
            profile.put("panNumber", (latest.getPanNumber() != null && !latest.getPanNumber().isEmpty()) 
                    ? AESUtil.decrypt(latest.getPanNumber()) : "");
            
            profile.put("address", latest.getAddress() != null ? latest.getAddress() : "");
        } else {
            profile.put("name", "Donor");
            profile.put("email", email);
            profile.put("mobile", "");
            profile.put("adharCard", "");
            profile.put("panNumber", "");
            profile.put("address", "");
        }

        double total = donations.stream().mapToDouble(d -> d.getDonationAmount() != null ? d.getDonationAmount() : 0.0).sum();
        profile.put("totalDonated", total);
        profile.put("donationCount", donations.size());

        return profile;
    }

    @GetMapping("/my-donations")
    public List<Donation> getMyDonations() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (email == null || email.equals("anonymousUser")) {
            throw new RuntimeException("User not authenticated");
        }

        return donationRepository.findByEmail(email);
    }

    @PostMapping("/create-order")
    public String createOrder(@RequestParam double amount) {
        return donationService.createOrder(amount);
    }

    @PostMapping("/verify")
    public String verifyPayment(@RequestBody Map<String, Object> data) {
        try {
            Donation donation = new Donation();
            donation.setName((String) data.get("customerName"));
            donation.setEmail((String) data.get("customerEmail"));
            donation.setMobile(data.get("customerContact") != null ? data.get("customerContact").toString() : null);

            donation.setAdharCard((String) data.get("adharCard"));
            donation.setPanNumber((String) data.get("panNumber"));
            
            donation.setAddress((String) data.get("address"));
            donation.setDonationAmount(Double.valueOf(data.get("amount").toString()));
            donation.setProductId(data.get("productId") != null ? Long.valueOf(data.get("productId").toString()) : null);
            
            donation.setOrderId((String) data.get("orderId"));
            donation.setPaymentId((String) data.get("paymentId"));
            donation.setStatus("SUCCESS");
            donation.setPaymentDate(LocalDateTime.now());
            donation.setPaymentMode("RAZORPAY");

            donationService.saveDonation(donation);

            return "Payment Verified Successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/admin/all-donations")
    public List<Donation> getAllDonationsWithFilter(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        if (startDate != null && endDate != null) {
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
            return donationRepository.findByPaymentDateBetweenOrderByPaymentDateDesc(start, end);
        }
        return donationRepository.findAllByOrderByPaymentDateDesc();
    }

    // ==================== FIXED RECEIPT DOWNLOAD WITH ROLE AUTH ====================
    @GetMapping("/receipt/{id}")
    public ResponseEntity<byte[]> getReceipt(@PathVariable Long id) {
        try {
            Donation donation = donationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Donation not found with id: " + id));

            // 1. Professional Authentication Check
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = auth.getName();
            
            // Check for specific authorities from your Jwt Filter
            boolean isAdminOrManager = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

            // 2. Security Gate: Only allow the owner OR an Admin/Manager
            if (!isAdminOrManager && !donation.getEmail().equals(currentEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // 3. Decrypt for PDF Generation
            if (donation.getAdharCard() != null && !donation.getAdharCard().isEmpty()) {
                try {
                    donation.setAdharCard(AESUtil.decrypt(donation.getAdharCard()));
                } catch (Exception e) { donation.setAdharCard("Error Decrypting"); }
            }
            if (donation.getPanNumber() != null && !donation.getPanNumber().isEmpty()) {
                try {
                    donation.setPanNumber(AESUtil.decrypt(donation.getPanNumber()));
                } catch (Exception e) { donation.setPanNumber("Error Decrypting"); }
            }

            // 4. Generate PDF using the new format method in Service
            byte[] pdfBytes = donationService.generateReceiptPdf(donation);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Professional filename attachment
            String filename = "receipt_" + (donation.getOrderId() != null ? donation.getOrderId() : donation.getId()) + ".pdf";
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/admin/offline-donation")
    public ResponseEntity<String> addOfflineDonation(@RequestBody Map<String, Object> data) {
        try {
            Donation donation = new Donation();
            donation.setName((String) data.get("name"));
            donation.setEmail((String) data.get("email"));
            donation.setMobile((String) data.get("mobile"));
            
            donation.setAdharCard((String) data.get("adharCard"));
            donation.setPanNumber((String) data.get("panNumber"));
            
            donation.setAddress((String) data.get("address"));
            donation.setDonationAmount(Double.valueOf(data.get("donationAmount").toString()));
            donation.setProductId(data.get("productId") != null ? Long.valueOf(data.get("productId").toString()) : null);
            donation.setPaymentMode((String) data.get("paymentMode"));
            donation.setReferenceNumber((String) data.get("referenceNumber"));
            donation.setStatus("SUCCESS");
            donation.setPaymentDate(LocalDateTime.now());
            donation.setOffline(true);

            donationService.saveDonation(donation);

            return ResponseEntity.ok("Offline Donation Added Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}