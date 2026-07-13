package com.abikumar.coffee_reservation.controller;

import com.abikumar.coffee_reservation.dto.ApiResponseDto;
import com.abikumar.coffee_reservation.dto.LoginResponseDto;
import com.abikumar.coffee_reservation.dto.SendOtpRequestDto;
import com.abikumar.coffee_reservation.dto.VerifyOtpRequestDto;
import com.abikumar.coffee_reservation.service.OtpService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Customer facing email-OTP login. There is no sign-up: any email can request
 * an OTP and, once verified, is treated as a logged-in customer.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final OtpService otpService;

	public AuthController(OtpService otpService) {
		this.otpService = otpService;
	}

	@PostMapping("/send-otp")
	public ApiResponseDto<Void> sendOtp(@Valid @RequestBody SendOtpRequestDto requestDto) {
		log.info("Received OTP request for email={}", requestDto.getEmail());
		otpService.sendOtp(requestDto.getEmail());
		return ApiResponseDto.success("OTP sent to your email address.");
	}

	@PostMapping("/verify-otp")
	public ApiResponseDto<LoginResponseDto> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto requestDto) {
		LoginResponseDto loginResponseDto = otpService.verifyOtp(requestDto.getEmail(), requestDto.getOtp());
		return ApiResponseDto.success("Login successful.", loginResponseDto);
	}
}
