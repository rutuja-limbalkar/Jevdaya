package com.jevdaya.serviceImpl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jevdaya.Entity.Payment;
import com.jevdaya.repo.PaymentRepository;
import com.jevdaya.service.PaymentService;
import jakarta.mail.util.ByteArrayDataSource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.util.Date;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String KEY_ID = "rzp_test_SahHHcqfDLxNc9";
    private static final String KEY_SECRET = "6U69PBnxMGffotooyDPcF5rK";

    @Autowired
    private PaymentRepository repo;

    @Autowired
    private JavaMailSender javaMailSender;   // ✅ Spring Boot auto-configures this

    @Override
    public String createOrder(double amount) {
        try {
            RazorpayClient client = new RazorpayClient(KEY_ID, KEY_SECRET);

            JSONObject options = new JSONObject();
            options.put("amount", (int) (amount * 100)); // paise
            options.put("currency", "INR");
            options.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = client.orders.create(options);
            return order.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"Order creation failed\"}";
        }
    }

    @Override
    public String verifyPayment(String orderId, String paymentId, String signature, Double amount, String customerName,
			String customerEmail, Long customerContact) {

        try {
            // ==================== TESTING MODE (kept exactly as you had) ====================
            boolean signatureValid = true;   // ← Test mode: always success

            if (signatureValid) {
                Payment p = new Payment();
                p.setOrderId(orderId);
                p.setPaymentId(paymentId);
                p.setSignature(signature);
                p.setAmount(amount);
                p.setStatus("SUCCESS");
                p.setCustomerName(customerName);      // ✅ NEW
                p.setCustomerEmail(customerEmail);
                p.setCustomerContact(customerContact);// ✅ NEW

                repo.save(p);

                // ✅ GENERATE PDF RECEIPT
                byte[] pdfBytes = generateReceiptPdf(p);

                // ✅ SEND EMAIL WITH PDF ATTACHMENT
                if (customerEmail != null && !customerEmail.isEmpty()) {
                    sendEmailWithAttachment(
                            customerEmail,
                            "Jevdaya Payment Receipt - " + orderId,
                            "Dear " + (customerName != null ? customerName : "Valued Supporter") + ",\n\n" +
                                    "Thank you for your generous support to Jevdaya!\n\n" +
                                    "Please find your payment receipt attached.\n\n" +
                                    "Regards,\nJevdaya Team",
                            pdfBytes,
                            "receipt_" + orderId + ".pdf"
                    );
                }

                return "Payment Verified";
            } else {
                return "Invalid Signature";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // ==================== NEW HELPER METHODS ====================

    private byte[] generateReceiptPdf(Payment payment) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Header
            Paragraph title = new Paragraph("Jevdaya Payment Receipt");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Customer Name : " + payment.getCustomerName()));
            document.add(new Paragraph("Customer Email: " + payment.getCustomerEmail()));
            document.add(new Paragraph("Customer Contact: " + 
            	    (payment.getCustomerContact() != null ? payment.getCustomerContact() : "N/A")));
            document.add(new Paragraph("Order ID      : " + payment.getOrderId()));
            document.add(new Paragraph("Payment ID    : " + payment.getPaymentId()));
            document.add(new Paragraph("Amount Paid   : ₹" + payment.getAmount()));
            document.add(new Paragraph("Status        : " + payment.getStatus()));
            document.add(new Paragraph("Date          : " + new Date()));
            document.add(new Paragraph(" "));

            Paragraph thanks = new Paragraph("Thank you for your support to Jevdaya ❤️");
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private void sendEmailWithAttachment(String to, String subject, String bodyText,
                                         byte[] attachment, String fileName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(bodyText, false);
            helper.addAttachment(fileName, new ByteArrayDataSource(attachment, "application/pdf"));

            javaMailSender.send(message);
            System.out.println("✅ Receipt emailed to: " + to);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }


}