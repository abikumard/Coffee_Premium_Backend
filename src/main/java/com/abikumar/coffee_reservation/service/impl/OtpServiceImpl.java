package com.abikumar.coffee_reservation.service.impl;

import com.abikumar.coffee_reservation.constant.AppConstants;
import com.abikumar.coffee_reservation.dao.OtpDao;
import com.abikumar.coffee_reservation.dto.LoginResponseDto;
import com.abikumar.coffee_reservation.entity.AuthToken;
import com.abikumar.coffee_reservation.entity.OtpVerification;
import com.abikumar.coffee_reservation.exception.InvalidOtpException;
import com.abikumar.coffee_reservation.service.AuthTokenService;
import com.abikumar.coffee_reservation.service.EmailService;
import com.abikumar.coffee_reservation.service.OtpService;
import com.abikumar.coffee_reservation.util.OtpGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpServiceImpl.class);

    private final OtpDao otpDao;
    private final EmailService emailService;
    private final AuthTokenService authTokenService;

    @Value("${otp.expiry.minutes:5}")
    private int otpExpiryMinutes;

    public OtpServiceImpl(OtpDao otpDao, EmailService emailService, AuthTokenService authTokenService) {
        this.otpDao = otpDao;
        this.emailService = emailService;
        this.authTokenService = authTokenService;
    }

    @Override
    public void sendOtp(String email) {
        String otp = OtpGenerator.generateSixDigitOtp();
        LocalDateTime now = LocalDateTime.now();

        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(email);
        otpVerification.setOtp(otp);
        otpVerification.setCreatedAt(now);
        otpVerification.setExpiresAt(now.plusMinutes(otpExpiryMinutes));
        otpVerification.setVerified(false);

        otpDao.insertOtp(otpVerification);
        emailService.sendOtpEmail(email, otp);
        log.info("OTP generated and sent for email={}", email);
    }

    @Override
    public LoginResponseDto verifyOtp(String email, String otp) {
        OtpVerification latest = otpDao.findLatestByEmail(email)
                .orElseThrow(() -> new InvalidOtpException("No OTP was requested for this email"));

        if (latest.isVerified()) {
            throw new InvalidOtpException("This OTP has already been used. Please request a new one.");
        }
        if (latest.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP has expired. Please request a new one.");
        }
        if (!latest.getOtp().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP. Please check and try again.");
        }

        otpDao.markVerified(latest.getId());

        AuthToken authToken = authTokenService.issueToken(email, AppConstants.ROLE_CUSTOMER);
        log.info("OTP verified successfully for email={}", email);
        return new LoginResponseDto(authToken.getToken(), email, AppConstants.ROLE_CUSTOMER);
    }
}
