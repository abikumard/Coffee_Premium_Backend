package com.abikumar.coffee_reservation.controller;

import com.abikumar.coffee_reservation.dto.ApiResponseDto;
import com.abikumar.coffee_reservation.dto.ReservationRequestDto;
import com.abikumar.coffee_reservation.dto.ReservationResponseDto;
import com.abikumar.coffee_reservation.security.TokenAuthInterceptor;
import com.abikumar.coffee_reservation.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Customer submits a reservation request. Requires a valid logged-in (OTP verified)
     * session token. Always saved with status = PENDING; never auto-confirmed.
     */
    @PostMapping
    public ApiResponseDto<Map<String, Long>> createReservation(@Valid @RequestBody ReservationRequestDto requestDto) {
        Long id = reservationService.createReservation(requestDto);
        log.info("Reservation request submitted, id={}", id);
        return ApiResponseDto.success(
                "Your reservation request has been submitted successfully. " +
                        "You will receive a confirmation email once your reservation is reviewed by our team.",
                Map.of("id", id));
    }

    /**
     * Any logged-in customer: their own reservations (any status), most recent first.
     * Powers the "Your Bookings" page in the frontend.
     */
    @GetMapping("/my")
    public ApiResponseDto<List<ReservationResponseDto>> getMyReservations(HttpServletRequest request) {
        String email = (String) request.getAttribute(TokenAuthInterceptor.REQUEST_ATTR_EMAIL);
        return ApiResponseDto.success("Your reservations fetched.", reservationService.getMyReservations(email));
    }

    /**
     * Admin only: list of every reservation awaiting a decision.
     */
    @GetMapping("/pending")
    public ApiResponseDto<List<ReservationResponseDto>> getPendingReservations() {
        return ApiResponseDto.success("Pending reservations fetched.", reservationService.getPendingReservations());
    }

    /**
     * Admin only: list of every reservation that has already been approved.
     */
    @GetMapping("/approved")
    public ApiResponseDto<List<ReservationResponseDto>> getApprovedReservations() {
        return ApiResponseDto.success("Approved reservations fetched.", reservationService.getApprovedReservations());
    }

    /**
     * Admin only: every decided reservation (approved or rejected) for the History page.
     */
    @GetMapping("/history")
    public ApiResponseDto<List<ReservationResponseDto>> getReservationHistory() {
        return ApiResponseDto.success("Reservation history fetched.", reservationService.getReservationHistory());
    }

    /**
     * Admin only: approves a pending reservation and triggers the confirmation email.
     */
    @PutMapping("/{id}/approve")
    public ApiResponseDto<Void> approveReservation(@PathVariable Long id, HttpServletRequest request) {
        String adminEmail = (String) request.getAttribute(TokenAuthInterceptor.REQUEST_ATTR_EMAIL);
        reservationService.approveReservation(id, adminEmail);
        return ApiResponseDto.success("Reservation approved and confirmation email sent.");
    }

    /**
     * Admin only: rejects a pending reservation and triggers the rejection email.
     */
    @PutMapping("/{id}/reject")
    public ApiResponseDto<Void> rejectReservation(@PathVariable Long id, HttpServletRequest request) {
        String adminEmail = (String) request.getAttribute(TokenAuthInterceptor.REQUEST_ATTR_EMAIL);
        reservationService.rejectReservation(id, adminEmail);
        return ApiResponseDto.success("Reservation rejected and notification email sent.");
    }
}
