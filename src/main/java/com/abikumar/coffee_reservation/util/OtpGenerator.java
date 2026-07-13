package com.abikumar.coffee_reservation.util;

import java.security.SecureRandom;

public final class OtpGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private OtpGenerator() {
    }

    /**
     * Generates a random 6 digit numeric OTP, e.g. "042917".
     */
    public static String generateSixDigitOtp() {
        int value = RANDOM.nextInt(1_000_000);
        return String.format("%06d", value);
    }
}
