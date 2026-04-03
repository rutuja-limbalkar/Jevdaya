//package com.jevdaya.serviceImpl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Random;
//
//@Service
//public class OtpService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    // ✅ Store OTP in memory (email → otp)
//    // In real production use Redis, but for learning this is fine
//    private Map<String, String> otpStorage = new HashMap<>();
//
//    // ✅ Generate 6-digit OTP and send to email
//    public void sendOtp(String email) {
//        // Generate random 6-digit OTP
//        String otp = String.valueOf(100000 + new Random().nextInt(900000));
//
//        // Save it (email -> otp)
//        otpStorage.put(email, otp);
//
//        // Send email
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Your OTP for Registration - Jevdaya");
//        message.setText("Hello!\n\nYour OTP for registration is: " + otp +
//                        "\n\nThis OTP is valid for 10 minutes.\n\nDo not share this with anyone.\n\nJevdaya Team");
//
//        mailSender.send(message);
//
//        System.out.println("OTP sent to: " + email + " | OTP: " + otp); // for testing
//    }
//
//    // ✅ Verify OTP entered by user
//    public boolean verifyOtp(String email, String enteredOtp) {
//        String savedOtp = otpStorage.get(email);
//
//        if (savedOtp != null && savedOtp.equals(enteredOtp)) {
//            otpStorage.remove(email); // ✅ Remove after use (one-time use)
//            return true;
//        }
//        return false;
//    }
//}