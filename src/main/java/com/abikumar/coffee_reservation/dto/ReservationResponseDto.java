package com.abikumar.coffee_reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationResponseDto {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime reservationTime;

    private Integer partySize;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedDate;

    private String approvedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public Integer getPartySize() {
        return partySize;
    }

    public void setPartySize(Integer partySize) {
        this.partySize = partySize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public static ReservationResponseDto fromEntity(com.abikumar.coffee_reservation.entity.Reservation r) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setId(r.getId());
        dto.setFullName(r.getFullName());
        dto.setEmail(r.getEmail());
        dto.setPhoneNumber(r.getPhoneNumber());
        dto.setReservationDate(r.getReservationDate());
        dto.setReservationTime(r.getReservationTime());
        dto.setPartySize(r.getPartySize());
        dto.setStatus(r.getStatus());
        dto.setCreatedDate(r.getCreatedDate());
        dto.setApprovedDate(r.getApprovedDate());
        dto.setApprovedBy(r.getApprovedBy());
        return dto;
    }
}
