package com.abikumar.coffee_reservation.service;

import com.abikumar.coffee_reservation.dto.LoginResponseDto;

public interface OtpService {

    void sendOtp(String email);

    LoginResponseDto verifyOtp(String email, String otp);
}
