package com.abikumar.coffee_reservation.service.impl;

import com.abikumar.coffee_reservation.entity.Reservation;
import com.abikumar.coffee_reservation.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Talking to the SMTP server (Gmail) can take a few seconds per email. Every method
 * here is {@link Async} so the calling request (send-otp, approve, reject) can respond
 * to the browser immediately instead of making the user wait for the mail round-trip.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

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
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
            log.info("Email sent to={} subject='{}'", toEmail, subject);
        } catch (Exception ex) {
            // Email delivery failure should not break the calling flow (e.g. reservation
            // approval must still succeed even if the mail server is temporarily down).
            log.error("Failed to send email to={} subject='{}'", toEmail, subject, ex);
        }
    }
}
