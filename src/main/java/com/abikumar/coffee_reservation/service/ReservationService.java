package com.abikumar.coffee_reservation.service;

import com.abikumar.coffee_reservation.dto.ReservationRequestDto;
import com.abikumar.coffee_reservation.dto.ReservationResponseDto;

import java.util.List;

public interface ReservationService {

    Long createReservation(ReservationRequestDto requestDto);

    List<ReservationResponseDto> getPendingReservations();

    List<ReservationResponseDto> getApprovedReservations();

    /**
     * Every reservation (any status) submitted by the given logged-in customer,
     * most recent first. Used to power the "Your Bookings" page.
     */
    List<ReservationResponseDto> getMyReservations(String email);

    /**
     * Every decided reservation (approved or rejected) for the admin "History" page.
     */
    List<ReservationResponseDto> getReservationHistory();

    void approveReservation(Long id, String approvedByAdminEmail);

    void rejectReservation(Long id, String rejectedByAdminEmail);
}
