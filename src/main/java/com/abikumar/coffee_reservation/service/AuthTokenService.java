package com.abikumar.coffee_reservation.service;

import com.abikumar.coffee_reservation.entity.AuthToken;

public interface AuthTokenService {

    AuthToken issueToken(String email, String role);

    AuthToken validateToken(String token);
}
