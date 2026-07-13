package com.abikumar.coffee_reservation.entity;

import java.time.LocalDateTime;

public class AuthToken {

    private String token;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public AuthToken() {
    }

    public AuthToken(String token, String email, String role, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
