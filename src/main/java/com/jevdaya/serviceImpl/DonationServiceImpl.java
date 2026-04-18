package com.jevdaya.serviceImpl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jevday.util.AESUtil;
import com.jevday.util.EmailTemplateUtil;
import com.jevdaya.Entity.Donation;
import com.jevdaya.repo.DonationRepository;
import com.jevdaya.service.DonationService;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;

import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class DonationServiceImpl implements DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String KEY_ID = "rzp_test_SahHHcqfDLxNc9";
    private static final String KEY_SECRET = "6U69PBnxMGffotooyDPcF5rK";

    @Override
    @Transactional
    public Donation saveDonation(Donation donation) {
        donation.setId(null);

        // Encryption logic...
        if (donation.getAdharCard() != null && !donation.getAdharCard().isBlank()) {
            String raw = donation.getAdharCard().trim();
            if (!raw.matches("^\\d{12}$")) throw new RuntimeException("Aadhaar must be 12 digits");
            donation.setAdharCard(AESUtil.encrypt(raw));
        }
        // PAN logic...
        if (donation.getPanNumber() != null && !donation.getPanNumber().isBlank()) {
            String rawPan = donation.getPanNumber().trim().toUpperCase();
            donation.setPanNumber(AESUtil.encrypt(rawPan));
        }

        donation.setPaymentDate(LocalDateTime.now());
        
        // Save to DB
        Donation savedDonation = donationRepository.save(donation);
        
        // 🔥 CRITICAL: Call the email method here!
        // We create a copy for the email so we can decrypt values for the PDF
        try {
            Donation forReceipt = new Donation();
            // Copy values manually or use BeanUtils
            forReceipt.setName(savedDonation.getName());
            forReceipt.setEmail(savedDonation.getEmail());
            forReceipt.setMobile(savedDonation.getMobile());
            forReceipt.setDonationAmount(savedDonation.getDonationAmount());
            forReceipt.setOrderId(savedDonation.getOrderId());
            forReceipt.setPaymentId(savedDonation.getPaymentId());
            forReceipt.setStatus(savedDonation.getStatus());

            // Call the email sender
            sendReceiptEmail(forReceipt);
        } catch (Exception e) {
            System.err.println("Database saved, but email failed: " + e.getMessage());
        }

        return savedDonation;
    }
    @Override
    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    @Override
    public String createOrder(double amount) {
        try {
            RazorpayClient client = new RazorpayClient(KEY_ID, KEY_SECRET);
            JSONObject options = new JSONObject();
            options.put("amount", (int) (amount * 100));
            options.put("currency", "INR");
            options.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = client.orders.create(options);
            return order.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"Order creation failed\"}";
        }
    }

 // Inside DonationServiceImpl.java

    private void sendReceiptEmail(Donation donation) {
        try {
            // 1. Generate PDF attachment
            byte[] pdfBytes = generateReceiptPdf(donation);

            // 2. Prepare Email
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(donation.getEmail());
            helper.setSubject("Jevdaya Donation Receipt - " + (donation.getPaymentId() != null ? donation.getPaymentId() : "Offline"));

            // 3. GET HTML FROM UTILITY CLASS (This is the professional way)
            String htmlContent = EmailTemplateUtil.getReceiptHtml(donation);
            
            // Set 'true' to indicate this is an HTML email
            helper.setText(htmlContent, true);

            // 4. Add PDF as attachment
            String fileName = "Receipt_" + (donation.getPaymentId() != null ? donation.getPaymentId() : donation.getId()) + ".pdf";
            helper.addAttachment(fileName, new ByteArrayDataSource(pdfBytes, "application/pdf"));

            // 5. Send
            javaMailSender.send(message);
            System.out.println("Receipt Email Sent Successfully to: " + donation.getEmail());

        } catch (Exception e) {
            System.err.println("Error sending receipt email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public byte[] generateReceiptPdf(Donation donation) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseColor redColor = new BaseColor(255, 0, 0);
            Font redFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, redColor);
            Font redTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, redColor);
            Font blackFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font blackBoldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            
            String dateStr = donation.getPaymentDate() != null ? 
                donation.getPaymentDate().toLocalDate().toString() : "N/A";

            // Title
            Paragraph title = new Paragraph("Payment Receipt", redTitleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Hindi Address
            Paragraph hindiAddress = new Paragraph("बीतराग, सेक. २४, प्लाट नंबर ६, निगडी प्राधिकरण, निगडी, पुणे - ४११०४४", redFont);
            hindiAddress.setAlignment(Element.ALIGN_CENTER);
            document.add(hindiAddress);
            
            // Register Info
            Paragraph registerInfo = new Paragraph("रजिस्टर नंबर : महा. ३७०/२०१४/पुणे @ visit us on: www.jeevdaya.net", blackFont);
            registerInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(registerInfo);
            
            document.add(new Paragraph(" "));
            
            // 80G Registration Table
            PdfPTable regTable = new PdfPTable(3);
            regTable.setWidthPercentage(100);
            regTable.setSpacingBefore(5);
            regTable.setSpacingAfter(5);
            
            PdfPCell cell1 = new PdfPCell(new Phrase("आयकर अधिनियम कि धारा 80G के अंतर्गत रजिस्टर्ड", smallFont));
            cell1.setPadding(5);
            regTable.addCell(cell1);
            
            PdfPCell cell2 = new PdfPCell(new Phrase("DI No. AAA TS 1209 HE 2021901\nUR No. AAA TS 1209 HE 202191", smallFont));
            cell2.setPadding(5);
            regTable.addCell(cell2);
            
            PdfPCell cell3 = new PdfPCell(new Phrase("DI No. AAA TS 1209 HF 2021701\nUR No. AAA TS 1209 HF 202171", smallFont));
            cell3.setPadding(5);
            regTable.addCell(cell3);
            
            document.add(regTable);
            
            document.add(new Paragraph(" "));
            
            // Receipt No & Date
            Paragraph meta = new Paragraph();
            Chunk receiptNoLabel = new Chunk("Receipt no: ", redFont);
            Chunk receiptNoValue = new Chunk("JIV" + donation.getId(), blackBoldFont);
            Chunk dateLabel = new Chunk("                                          Date: ", redFont);
            Chunk dateValue = new Chunk(dateStr, blackBoldFont);
            meta.add(receiptNoLabel);
            meta.add(receiptNoValue);
            meta.add(dateLabel);
            meta.add(dateValue);
            document.add(meta);
            
            // Separator line
            Paragraph line = new Paragraph("______________________________________________________________________________");
            document.add(line);
            
            document.add(new Paragraph(" "));
            
            // Received with thanks from
            Paragraph receivedFrom = new Paragraph();
            receivedFrom.add(new Chunk("Received with thanks from: ", redFont));
            receivedFrom.add(new Chunk(donation.getName() != null ? donation.getName() : "N/A", blackFont));
            document.add(receivedFrom);
            
            // Address & Cell
            Paragraph addressCell = new Paragraph();
            addressCell.add(new Chunk("Address: ", redFont));
            addressCell.add(new Chunk(donation.getAddress() != null ? donation.getAddress() : "N/A", blackFont));
            addressCell.add(new Chunk("                             Cell: ", redFont));
            addressCell.add(new Chunk(donation.getMobile() != null ? donation.getMobile() : "N/A", blackFont));
            document.add(addressCell);
            
            document.add(new Paragraph(" "));
            
            // Sum of Rupees
            Paragraph sumRupees = new Paragraph();
            sumRupees.add(new Chunk("the sum of Rupees: ", redFont));
            sumRupees.add(new Chunk(donation.getDonationAmount() != null ? donation.getDonationAmount() + " Only" : "0 Only", blackFont));
            document.add(sumRupees);
            
            // Payment details
            Paragraph paymentDetails = new Paragraph();
            paymentDetails.add(new Chunk("by: ", redFont));
            paymentDetails.add(new Chunk(donation.getPaymentMode() != null ? donation.getPaymentMode() : "N/A", blackFont));
            paymentDetails.add(new Chunk("  Txn. No.: ", redFont));
            paymentDetails.add(new Chunk(donation.getPaymentId() != null ? donation.getPaymentId() : 
                (donation.getReferenceNumber() != null ? donation.getReferenceNumber() : "N/A"), blackFont));
            paymentDetails.add(new Chunk("   dated: ", redFont));
            paymentDetails.add(new Chunk(dateStr, blackFont));
            document.add(paymentDetails);
            
            document.add(new Paragraph(" "));
            
            // Amount Box
            PdfPTable amtTable = new PdfPTable(2);
            amtTable.setTotalWidth(220);
            amtTable.setLockedWidth(true);
            amtTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            PdfPCell rupeeCell = new PdfPCell(new Phrase("₹:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, redColor)));
            rupeeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            rupeeCell.setPadding(10);
            amtTable.addCell(rupeeCell);
            
            PdfPCell amountCell = new PdfPCell(new Phrase(
                donation.getDonationAmount() != null ? donation.getDonationAmount().toString() : "0", 
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountCell.setPadding(10);
            amtTable.addCell(amountCell);
            document.add(amtTable);
            
            document.add(new Paragraph(" "));
            
            // Footer
            Paragraph footer = new Paragraph("Payment Receipt.", redFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10);
            document.add(footer);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) { 
            throw new RuntimeException(e); 
        }
    }
}