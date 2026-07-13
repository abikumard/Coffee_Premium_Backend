package com.abikumar.coffee_reservation.service.impl;

import com.abikumar.coffee_reservation.dao.AuthTokenDao;
import com.abikumar.coffee_reservation.entity.AuthToken;
import com.abikumar.coffee_reservation.exception.UnauthorizedException;
import com.abikumar.coffee_reservation.service.AuthTokenService;
import com.abikumar.coffee_reservation.util.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    private static final Logger log = LoggerFactory.getLogger(AuthTokenServiceImpl.class);

    private final AuthTokenDao authTokenDao;

    @Value("${auth.token.expiry.hours:12}")
    private int tokenExpiryHours;

    public AuthTokenServiceImpl(AuthTokenDao authTokenDao) {
        this.authTokenDao = authTokenDao;
    }

    @Override
    public AuthToken issueToken(String email, String role) {
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = new AuthToken(
                TokenGenerator.generateToken(),
                email,
                role,
                now,
                now.plusHours(tokenExpiryHours)
        );
        authTokenDao.insertToken(authToken);
        return authToken;
    }

    @Override
    public AuthToken validateToken(String token) {
        AuthToken authToken = authTokenDao.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired session. Please login again."));

        if (authToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Token expired for email={}", authToken.getEmail());
            throw new UnauthorizedException("Session expired. Please login again.");
        }
        return authToken;
    }
}
