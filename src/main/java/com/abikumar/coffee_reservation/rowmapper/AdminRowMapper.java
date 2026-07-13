package com.abikumar.coffee_reservation.rowmapper;

import com.abikumar.coffee_reservation.entity.Admin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AdminRowMapper implements RowMapper<Admin> {

    @Override
    public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getLong("id"));
        admin.setEmail(rs.getString("email"));
        admin.setPassword(rs.getString("password"));
        Timestamp createdDate = rs.getTimestamp("created_date");
        admin.setCreatedDate(createdDate != null ? createdDate.toLocalDateTime() : null);
        return admin;
    }
}
