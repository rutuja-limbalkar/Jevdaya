package com.jevday.util;

import com.jevdaya.Entity.Donation;
import java.time.format.DateTimeFormatter;

public class EmailTemplateUtil {

    public static String getReceiptHtml(Donation donation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = donation.getPaymentDate() != null ? donation.getPaymentDate().format(formatter) : "N/A";
        
        return "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "    <meta charset='UTF-8'>" +
            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "    <title>Jeevdaya Donation Receipt</title>" +
            "    <style>" +
            "        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }" +
            "        .receipt { max-width: 800px; margin: 0 auto; background: white; border: 1px solid #ddd; padding: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }" +
            "        .text-center { text-align: center; }" +
            "        .red { color: #ff0000; }" +
            "        .red-bold { color: #ff0000; font-weight: bold; }" +
            "        .title { font-size: 28px; font-weight: bold; margin-bottom: 15px; }" +
            "        .address { font-size: 14px; font-weight: bold; margin: 5px 0; }" +
            "        .register-info { font-size: 12px; margin: 5px 0; }" +
            "        .reg-table { width: 100%; border-collapse: collapse; margin: 15px 0; font-size: 10px; }" +
            "        .reg-table td { border: 1px solid black; padding: 8px; text-align: center; vertical-align: top; }" +
            "        .meta { margin: 15px 0 5px 0; }" +
            "        .separator { border-top: 1px solid black; margin: 10px 0; }" +
            "        .info-row { margin: 12px 0; font-size: 14px; }" +
            "        .amount-box { text-align: center; margin: 30px 0; }" +
            "        .amount-table { margin: 0 auto; border-collapse: collapse; }" +
            "        .amount-table td { border: 1px solid black; padding: 10px 40px; font-size: 20px; font-weight: bold; }" +
            "        .amount-table td:first-child { color: #ff0000; }" +
            "        .footer { text-align: center; color: #ff0000; font-weight: bold; margin-top: 20px; }" +
            "        @media (max-width: 600px) { .receipt { padding: 15px; } .amount-table td { padding: 8px 20px; font-size: 16px; } }" +
            "    </style>" +
            "</head>" +
            "<body>" +
            "    <div class='receipt'>" +
            "        <div class='text-center'>" +
            "            <div class='title red'>Payment Receipt</div>" +
            "            <div class='address red'>बीतराग, सेक. २४, प्लाट नंबर ६, निगडी प्राधिकरण, निगडी, पुणे - ४११०४४</div>" +
            "            <div class='register-info'>रजिस्टर नंबर : महा. ३७०/२०१४/पुणे @ visit us on: www.jeevdaya.net</div>" +
            "        </div>" +
            "        <table class='reg-table'>" +
            "            <tr>" +
            "                <td>आयकर अधिनियम कि धारा 80G के अंतर्गत रजिस्टर्ड</td>" +
            "                <td>DI No. AAA TS 1209 HE 2021901<br>UR No. AAA TS 1209 HE 202191</td>" +
            "                <td>DI No. AAA TS 1209 HF 2021701<br>UR No. AAA TS 1209 HF 202171</td>" +
            "            </tr>" +
            "        </table>" +
            "        <div class='meta'>" +
            "            <span class='red-bold'>Receipt no:</span> <strong>JIV" + donation.getId() + "</strong>" +
            "            <span style='float: right;'><span class='red-bold'>Date:</span> <strong>" + dateStr + "</strong></span>" +
            "        </div>" +
            "        <div style='clear: both;'></div>" +
            "        <div class='separator'></div>" +
            "        <div class='info-row'><span class='red-bold'>Received with thanks from:</span> " + (donation.getName() != null ? donation.getName() : "N/A") + "</div>" +
            "        <div class='info-row'>" +
            "            <span class='red-bold'>Address:</span> " + (donation.getAddress() != null ? donation.getAddress() : "N/A") +
            "            <span style='float: right;'><span class='red-bold'>Cell:</span> " + (donation.getMobile() != null ? donation.getMobile() : "N/A") + "</span>" +
            "        </div>" +
            "        <div style='clear: both;'></div>" +
            "        <div class='info-row'><span class='red-bold'>the sum of Rupees:</span> " + (donation.getDonationAmount() != null ? donation.getDonationAmount() + " Only" : "0 Only") + "</div>" +
            "        <div class='info-row'>" +
            "            <span class='red-bold'>by:</span> " + (donation.getPaymentMode() != null ? donation.getPaymentMode() : "N/A") +
            "            <span class='red-bold' style='margin-left: 15px;'>Txn. No.:</span> " + (donation.getPaymentId() != null ? donation.getPaymentId() : (donation.getReferenceNumber() != null ? donation.getReferenceNumber() : "N/A")) +
            "            <span style='float: right;'><span class='red-bold'>dated:</span> " + dateStr + "</span>" +
            "        </div>" +
            "        <div style='clear: both;'></div>" +
            "        <div class='amount-box'>" +
            "            <table class='amount-table'>" +
            "                <tr>" +
            "                    <td>₹:</td>" +
            "                    <td>" + (donation.getDonationAmount() != null ? donation.getDonationAmount() : "0") + "</td>" +
            "                </tr>" +
            "            </table>" +
            "        </div>" +
            "        <div class='footer'>Payment Receipt.</div>" +
            "    </div>" +
            "</body>" +
            "</html>";
    }
}