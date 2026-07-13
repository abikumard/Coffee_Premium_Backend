package com.abikumar.coffee_reservation.service;

import com.abikumar.coffee_reservation.dto.LoginResponseDto;

public interface AdminService {

    LoginResponseDto login(String email, String rawPassword);

    void seedDefaultAdminIfMissing();
}
