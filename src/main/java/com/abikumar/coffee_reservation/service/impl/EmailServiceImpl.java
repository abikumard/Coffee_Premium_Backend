package com.abikumar.coffee_reservation.service.impl;

import com.abikumar.coffee_reservation.entity.Reservation;
import com.abikumar.coffee_reservation.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Railway blocks outbound SMTP (ports 587 and 465 both time out), so instead of talking
 * to Gmail's SMTP server directly, we send mail through Brevo's HTTPS API (port 443,
 * never blocked). Every method here is {@link Async} so the calling request (send-otp,
 * approve, reject) can respond to the browser immediately instead of waiting on the
 * mail round-trip.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name:Roasthouse}")
    private String senderName;

    @Override
    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        String body = "Your Roasthouse login OTP is: " + otp +
                "\n\nThis code is valid for 5 minutes. Please do not share it with anyone." +
                "\n\nThank You.";
        sendMail(toEmail, "Your Login OTP", body);
    }

    @Override
    @Async
    public void sendReservationApprovedEmail(Reservation reservation) {
        String body = "Dear " + reservation.getFullName() + ",\n\n" +
                "Your reservation has been confirmed.\n\n" +
                "Reservation Details:\n" +
                "Date: " + reservation.getReservationDate().format(DATE_FORMATTER) + "\n" +
                "Time: " + reservation.getReservationTime().format(TIME_FORMATTER) + "\n" +
                "Party Size: " + reservation.getPartySize() + "\n\n" +
                "We look forward to serving you.\n\n" +
                "Thank You.";
        sendMail(reservation.getEmail(), "Reservation Confirmed", body);
    }

    @Override
    @Async
    public void sendReservationRejectedEmail(Reservation reservation) {
        String body = "Dear " + reservation.getFullName() + ",\n\n" +
                "Unfortunately we are unable to confirm your reservation for the selected date and time " +
                "because all tables are fully booked.\n\n" +
                "Please try another date or time.\n\n" +
                "Thank You.";
        sendMail(reservation.getEmail(), "Reservation Update", body);
    }

    private void sendMail(String toEmail, String subject, String body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", brevoApiKey);
            headers.set("accept", "application/json");
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> payload = Map.of(
                    "sender", Map.of("name", senderName, "email", senderEmail),
                    "to", List.of(Map.of("email", toEmail)),
                    "subject", subject,
                    "textContent", body
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            restTemplate.postForEntity(BREVO_API_URL, request, String.class);
            log.info("Email sent to={} subject='{}'", toEmail, subject);
        } catch (Exception ex) {
            // Email delivery failure should not break the calling flow (e.g. reservation
            // approval must still succeed even if the mail provider is temporarily down).
            log.error("Failed to send email to={} subject='{}'", toEmail, subject, ex);
        }
    }
}
