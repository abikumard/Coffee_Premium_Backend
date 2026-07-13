package com.abikumar.coffee_reservation.rowmapper;

import com.abikumar.coffee_reservation.entity.Reservation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ReservationRowMapper implements RowMapper<Reservation> {

    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getLong("id"));
        reservation.setFullName(rs.getString("full_name"));
        reservation.setEmail(rs.getString("email"));
        reservation.setPhoneNumber(rs.getString("phone_number"));
        reservation.setReservationDate(rs.getDate("reservation_date").toLocalDate());
        reservation.setReservationTime(rs.getTime("reservation_time").toLocalTime());
        reservation.setPartySize(rs.getInt("party_size"));
        reservation.setStatus(rs.getString("status"));

        Timestamp createdDate = rs.getTimestamp("created_date");
        reservation.setCreatedDate(createdDate != null ? createdDate.toLocalDateTime() : null);

        Timestamp approvedDate = rs.getTimestamp("approved_date");
        reservation.setApprovedDate(approvedDate != null ? approvedDate.toLocalDateTime() : null);

        reservation.setApprovedBy(rs.getString("approved_by"));
        return reservation;
    }
}
