package com.abikumar.coffee_reservation.service;

import com.abikumar.coffee_reservation.entity.Reservation;

public interface EmailService {

    void sendOtpEmail(String toEmail, String otp);

    void sendReservationApprovedEmail(Reservation reservation);

    void sendReservationRejectedEmail(Reservation reservation);
}
