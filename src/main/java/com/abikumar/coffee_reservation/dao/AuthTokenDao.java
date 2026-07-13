package com.abikumar.coffee_reservation.dao;

import com.abikumar.coffee_reservation.entity.AuthToken;

import java.util.Optional;

public interface AuthTokenDao {

    void insertToken(AuthToken authToken);

    Optional<AuthToken> findByToken(String token);

    void deleteToken(String token);
}
