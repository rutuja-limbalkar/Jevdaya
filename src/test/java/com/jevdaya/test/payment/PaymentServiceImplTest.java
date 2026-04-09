package com.jevdaya.test.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.jevdaya.Entity.Payment;
import com.jevdaya.repo.PaymentRepository;
import com.jevdaya.serviceImpl.PaymentServiceImpl;

public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyPaymentSuccess() {
        String orderId = "order_test123";
        String paymentId = "pay_test123";
        String signature = "testsignature";
        Double amount = 500.0;
        String customerName = "Rutuja Jain";
        String customerEmail = "rutuja@example.com";
        Long customerContact = 9087897545L;        // ✅ Added L (Long literal)

        String result = paymentService.verifyPayment(
                orderId, paymentId, signature, amount, 
                customerName, customerEmail, customerContact);

        assertNotNull(result);
        assertEquals("Payment Verified", result);
    }

    @Test
    void testSavePayment() {
        Payment payment = new Payment();
        payment.setOrderId("order_123");
        payment.setPaymentId("pay_123");
        payment.setStatus("SUCCESS");
        payment.setAmount(500.0);
        payment.setCustomerName("Rutuja Jain");
        payment.setCustomerEmail("rutuja@example.com");
        payment.setCustomerContact(9087897545L);     // ✅ Long with L

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment saved = paymentRepository.save(payment);

        assertNotNull(saved);
        assertEquals("SUCCESS", saved.getStatus());
        assertEquals(500.0, saved.getAmount());
        assertEquals("Rutuja Jain", saved.getCustomerName());
        assertEquals("rutuja@example.com", saved.getCustomerEmail());
        assertEquals(9087897545L, saved.getCustomerContact());
    }

    @Test
    void testVerifyPaymentWithCustomerDetails() {
        String orderId = "order_test456";
        String paymentId = "pay_test456";
        String signature = "testsignature";
        Double amount = 7000.0;
        String customerName = "Test User";
        String customerEmail = "test@example.com";
        Long customerContact = 9087567876L;          // ✅ Long with L

        String result = paymentService.verifyPayment(
                orderId, paymentId, signature, amount, 
                customerName, customerEmail, customerContact);

        assertNotNull(result);
        assertEquals("Payment Verified", result);
    }
}