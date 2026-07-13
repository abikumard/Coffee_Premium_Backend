package com.abikumar.coffee_reservation.dao.impl;

import com.abikumar.coffee_reservation.dao.AuthTokenDao;
import com.abikumar.coffee_reservation.entity.AuthToken;
import com.abikumar.coffee_reservation.rowmapper.AuthTokenRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class AuthTokenDaoImpl implements AuthTokenDao {

    private static final Logger log = LoggerFactory.getLogger(AuthTokenDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final AuthTokenRowMapper authTokenRowMapper = new AuthTokenRowMapper();

    public AuthTokenDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertToken(AuthToken authToken) {
        String sql = "INSERT INTO auth_token (token, email, role, created_at, expires_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                authToken.getToken(),
                authToken.getEmail(),
                authToken.getRole(),
                Timestamp.valueOf(authToken.getCreatedAt()),
                Timestamp.valueOf(authToken.getExpiresAt()));
        log.info("Issued auth token for email={} role={}", authToken.getEmail(), authToken.getRole());
    }

    @Override
    public Optional<AuthToken> findByToken(String token) {
        String sql = "SELECT * FROM auth_token WHERE token = ?";
        try {
            AuthToken authToken = jdbcTemplate.queryForObject(sql, authTokenRowMapper, token);
            return Optional.ofNullable(authToken);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteToken(String token) {
        String sql = "DELETE FROM auth_token WHERE token = ?";
        jdbcTemplate.update(sql, token);
    }
}
