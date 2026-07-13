package com.abikumar.coffee_reservation.util;

import java.util.UUID;

public final class TokenGenerator {

    private TokenGenerator() {
    }

    /**
     * Generates an opaque, random session token.
     * Not a JWT — validity is looked up in the auth_token table on every request.
     */
    public static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
    }
}
