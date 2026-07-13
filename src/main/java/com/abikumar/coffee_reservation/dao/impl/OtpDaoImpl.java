package com.abikumar.coffee_reservation.dao.impl;

import com.abikumar.coffee_reservation.dao.OtpDao;
import com.abikumar.coffee_reservation.entity.OtpVerification;
import com.abikumar.coffee_reservation.rowmapper.OtpVerificationRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class OtpDaoImpl implements OtpDao {

    private static final Logger log = LoggerFactory.getLogger(OtpDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final OtpVerificationRowMapper otpVerificationRowMapper = new OtpVerificationRowMapper();

    public OtpDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertOtp(OtpVerification otpVerification) {
        String sql = "INSERT INTO otp_verification (email, otp, created_at, expires_at, verified) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                otpVerification.getEmail(),
                otpVerification.getOtp(),
                Timestamp.valueOf(otpVerification.getCreatedAt()),
                Timestamp.valueOf(otpVerification.getExpiresAt()),
                otpVerification.isVerified());
        log.info("Stored OTP for email={}", otpVerification.getEmail());
    }

    @Override
    public Optional<OtpVerification> findLatestByEmail(String email) {
        String sql = "SELECT * FROM otp_verification WHERE email = ? ORDER BY created_at DESC LIMIT 1";
        try {
            OtpVerification otp = jdbcTemplate.queryForObject(sql, otpVerificationRowMapper, email);
            return Optional.ofNullable(otp);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void markVerified(Long id) {
        String sql = "UPDATE otp_verification SET verified = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
