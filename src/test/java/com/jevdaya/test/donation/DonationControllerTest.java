package com.jevdaya.test.donation;

import com.jevdaya.Entity.Donation;

import com.jevdaya.controller.DonationController;
import com.jevdaya.repo.DonationRepository;
import com.jevdaya.service.DonationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = DonationController.class,
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {
                com.jevdaya.SecurityConfig.class,
                com.jevdaya.JwtAuthenticationFilter.class
            }
        )
    }
)
@AutoConfigureMockMvc(addFilters = false)
class DonationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ✅ Manually create ObjectMapper instead of @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private DonationService donationService;

    @MockitoBean
    private DonationRepository donationRepository;

    private Donation sampleDonation;

    @BeforeEach
    void setUp() {
        sampleDonation = new Donation();
        sampleDonation.setId(1L);
        sampleDonation.setName("Rutuja jain");
        sampleDonation.setEmail("rutuja@example.com");
        sampleDonation.setMobile("9876543210");
        sampleDonation.setDonationAmount(500.0);
        sampleDonation.setStatus("SUCCESS");
        sampleDonation.setPaymentDate(LocalDateTime.now());
    }

    @Test
    void testVerifyPayment_Success() throws Exception {
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", "order_12345");
        payload.put("paymentId", "pay_98765");
        payload.put("signature", "test_signature_abc");
        payload.put("amount", 500.0);
        payload.put("customerName", "Rutuja Jain");
        payload.put("customerEmail", "rutuja@example.com");
        payload.put("customerContact", "9876543210");
        payload.put("adharCard", "123456789012");
        payload.put("panNumber", "ABCDE1234F");
        payload.put("address", "123 Test Address");


        payload.put("productId", 1);

        when(donationService.saveDonation(any(Donation.class))).thenReturn(sampleDonation);

        mockMvc.perform(post("/payment/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllDonations() throws Exception {
        when(donationService.getAllDonations()).thenReturn(Arrays.asList(sampleDonation));

        mockMvc.perform(get("/payment/donations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testCreateOrder() throws Exception {
        String mockOrderJson = "{\"id\":\"order_12345\",\"amount\":50000}";

        when(donationService.createOrder(500.0)).thenReturn(mockOrderJson);

        mockMvc.perform(post("/payment/create-order")
                        .param("amount", "500"))
                .andExpect(status().isOk())
                .andExpect(content().string(mockOrderJson));
    }
}