package com.abikumar.coffee_reservation.rowmapper;

import com.abikumar.coffee_reservation.entity.AuthToken;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenRowMapper implements RowMapper<AuthToken> {

    @Override
    public AuthToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthToken token = new AuthToken();
        token.setToken(rs.getString("token"));
        token.setEmail(rs.getString("email"));
        token.setRole(rs.getString("role"));
        token.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        token.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        return token;
    }
}
