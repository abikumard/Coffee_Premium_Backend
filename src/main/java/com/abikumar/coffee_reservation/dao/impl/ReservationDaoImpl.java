package com.abikumar.coffee_reservation.dao.impl;

import com.abikumar.coffee_reservation.dao.ReservationDao;
import com.abikumar.coffee_reservation.entity.Reservation;
import com.abikumar.coffee_reservation.rowmapper.ReservationRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDaoImpl implements ReservationDao {

    private static final Logger log = LoggerFactory.getLogger(ReservationDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final ReservationRowMapper reservationRowMapper = new ReservationRowMapper();

    public ReservationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insertPendingReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation " +
                "(full_name, email, phone_number, reservation_date, reservation_time, party_size, status, created_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reservation.getFullName());
            ps.setString(2, reservation.getEmail());
            ps.setString(3, reservation.getPhoneNumber());
            ps.setDate(4, java.sql.Date.valueOf(reservation.getReservationDate()));
            ps.setTime(5, java.sql.Time.valueOf(reservation.getReservationTime()));
            ps.setInt(6, reservation.getPartySize());
            ps.setString(7, reservation.getStatus());
            ps.setTimestamp(8, Timestamp.valueOf(reservation.getCreatedDate()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        Long generatedId = key != null ? key.longValue() : null;
        log.info("Inserted reservation with id={} for email={}", generatedId, reservation.getEmail());
        return generatedId;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findByStatus(String status) {
        String sql = "SELECT * FROM reservation WHERE status = ? ORDER BY reservation_date ASC, reservation_time ASC";
        return jdbcTemplate.query(sql, reservationRowMapper, status);
    }

    @Override
    public List<Reservation> findByEmail(String email) {
        String sql = "SELECT * FROM reservation WHERE email = ? ORDER BY created_date DESC";
        return jdbcTemplate.query(sql, reservationRowMapper, email);
    }

    @Override
    public List<Reservation> findHistory() {
        String sql = "SELECT * FROM reservation WHERE status IN ('APPROVED', 'REJECTED') " +
                "ORDER BY reservation_date ASC, reservation_time ASC";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public int updateStatus(Long id, String status, LocalDateTime approvedDate, String approvedBy) {
        String sql = "UPDATE reservation SET status = ?, approved_date = ?, approved_by = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql, status,
                approvedDate != null ? Timestamp.valueOf(approvedDate) : null,
                approvedBy, id);
        log.info("Updated reservation id={} to status={} ({} row(s) affected)", id, status, rows);
        return rows;
    }
}
