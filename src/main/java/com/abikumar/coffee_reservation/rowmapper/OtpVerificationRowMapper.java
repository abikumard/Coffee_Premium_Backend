package com.abikumar.coffee_reservation.rowmapper;

import com.abikumar.coffee_reservation.entity.OtpVerification;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OtpVerificationRowMapper implements RowMapper<OtpVerification> {

    @Override
    public OtpVerification mapRow(ResultSet rs, int rowNum) throws SQLException {
        OtpVerification otp = new OtpVerification();
        otp.setId(rs.getLong("id"));
        otp.setEmail(rs.getString("email"));
        otp.setOtp(rs.getString("otp"));
        otp.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        otp.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        otp.setVerified(rs.getBoolean("verified"));
        return otp;
    }
}
