package com.abikumar.coffee_reservation.constant;

/**
 * Central place for application-wide constant values.
 */
public final class AppConstants {

    private AppConstants() {
        // utility class, no instances
    }

    // Reservation statuses
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";

    // Roles
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_ADMIN = "ADMIN";

    // OTP
    public static final String OTP_PURPOSE_LOGIN = "LOGIN";

    // Header
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
}
