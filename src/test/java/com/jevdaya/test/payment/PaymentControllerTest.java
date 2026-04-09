package com.jevdaya.test.payment;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.jevdaya.JwtAuthenticationFilter;
import com.jevdaya.JwtUtil;
import com.jevdaya.controller.PaymentController;
import com.jevdaya.service.PaymentService;



@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)   // ⭐ IMPORTANT LINE
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private JwtUtil jwtUtil;
    @Test
    void testCreateOrder() throws Exception {

        when(paymentService.createOrder(500.0))
                .thenReturn("{\"id\":\"order_123\"}");

        mockMvc.perform(post("/payment/create-order")
                        .param("amount", "500"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":\"order_123\"}"));
    }
}