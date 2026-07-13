package com.abikumar.coffee_reservation.dao;

import com.abikumar.coffee_reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    Long insertPendingReservation(Reservation reservation);

    Optional<Reservation> findById(Long id);

    List<Reservation> findByStatus(String status);

    List<Reservation> findByEmail(String email);

    /**
     * Every reservation that has already been decided (approved or rejected),
     * most recently decided first. Powers the admin "History" page.
     */
    List<Reservation> findHistory();

    int updateStatus(Long id, String status, LocalDateTime approvedDate, String approvedBy);
}
