package com.abikumar.coffee_reservation.service.impl;

import com.abikumar.coffee_reservation.constant.AppConstants;
import com.abikumar.coffee_reservation.dao.ReservationDao;
import com.abikumar.coffee_reservation.dto.ReservationRequestDto;
import com.abikumar.coffee_reservation.dto.ReservationResponseDto;
import com.abikumar.coffee_reservation.entity.Reservation;
import com.abikumar.coffee_reservation.exception.InvalidRequestException;
import com.abikumar.coffee_reservation.exception.ResourceNotFoundException;
import com.abikumar.coffee_reservation.service.EmailService;
import com.abikumar.coffee_reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    // The restaurant only accepts bookings between 9:00 AM and 11:00 PM (last table).
    private static final LocalTime BOOKING_WINDOW_START = LocalTime.of(9, 0);
    private static final LocalTime BOOKING_WINDOW_END = LocalTime.of(23, 0);

    private final ReservationDao reservationDao;
    private final EmailService emailService;

    public ReservationServiceImpl(ReservationDao reservationDao, EmailService emailService) {
        this.reservationDao = reservationDao;
        this.emailService = emailService;
    }

    @Override
    public Long createReservation(ReservationRequestDto requestDto) {
        LocalTime requestedTime = requestDto.getReservationTime();
        if (requestedTime == null
                || requestedTime.isBefore(BOOKING_WINDOW_START)
                || requestedTime.isAfter(BOOKING_WINDOW_END)) {
            throw new InvalidRequestException("Reservations are only accepted between 9:00 AM and 11:00 PM.");
        }

        Reservation reservation = new Reservation();
        reservation.setFullName(requestDto.getFullName());
        reservation.setEmail(requestDto.getEmail());
        reservation.setPhoneNumber(requestDto.getPhoneNumber());
        reservation.setReservationDate(requestDto.getReservationDate());
        reservation.setReservationTime(requestDto.getReservationTime());
        reservation.setPartySize(requestDto.getPartySize());
        reservation.setStatus(AppConstants.STATUS_PENDING);
        reservation.setCreatedDate(LocalDateTime.now());

        Long id = reservationDao.insertPendingReservation(reservation);
        log.info("New reservation request created id={} email={}", id, requestDto.getEmail());
        return id;
    }

    @Override
    public List<ReservationResponseDto> getPendingReservations() {
        return reservationDao.findByStatus(AppConstants.STATUS_PENDING).stream()
                .map(ReservationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponseDto> getApprovedReservations() {
        return reservationDao.findByStatus(AppConstants.STATUS_APPROVED).stream()
                .map(ReservationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponseDto> getMyReservations(String email) {
        if (email == null || email.isBlank()) {
            return List.of();
        }
        return reservationDao.findByEmail(email).stream()
                .map(ReservationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponseDto> getReservationHistory() {
        return reservationDao.findHistory().stream()
                .map(ReservationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void approveReservation(Long id, String approvedByAdminEmail) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        LocalDateTime approvedDate = LocalDateTime.now();
        reservationDao.updateStatus(id, AppConstants.STATUS_APPROVED, approvedDate, approvedByAdminEmail);

        reservation.setStatus(AppConstants.STATUS_APPROVED);
        reservation.setApprovedDate(approvedDate);
        reservation.setApprovedBy(approvedByAdminEmail);

        emailService.sendReservationApprovedEmail(reservation);
        log.info("Reservation id={} approved by={}", id, approvedByAdminEmail);
    }

    @Override
    public void rejectReservation(Long id, String rejectedByAdminEmail) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        LocalDateTime approvedDate = LocalDateTime.now();
        reservationDao.updateStatus(id, AppConstants.STATUS_REJECTED, approvedDate, rejectedByAdminEmail);

        reservation.setStatus(AppConstants.STATUS_REJECTED);
        reservation.setApprovedDate(approvedDate);
        reservation.setApprovedBy(rejectedByAdminEmail);

        emailService.sendReservationRejectedEmail(reservation);
        log.info("Reservation id={} rejected by={}", id, rejectedByAdminEmail);
    }
}
