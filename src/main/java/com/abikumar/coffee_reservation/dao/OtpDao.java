package com.abikumar.coffee_reservation.dao;

import com.abikumar.coffee_reservation.entity.OtpVerification;

import java.util.Optional;

public interface OtpDao {

    void insertOtp(OtpVerification otpVerification);

    Optional<OtpVerification> findLatestByEmail(String email);

    void markVerified(Long id);
}
