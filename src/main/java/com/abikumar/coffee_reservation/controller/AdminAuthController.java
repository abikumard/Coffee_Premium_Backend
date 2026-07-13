package com.abikumar.coffee_reservation.controller;

import com.abikumar.coffee_reservation.dto.AdminLoginRequestDto;
import com.abikumar.coffee_reservation.dto.ApiResponseDto;
import com.abikumar.coffee_reservation.dto.LoginResponseDto;
import com.abikumar.coffee_reservation.service.AdminService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private static final Logger log = LoggerFactory.getLogger(AdminAuthController.class);

    private final AdminService adminService;

    public AdminAuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ApiResponseDto<LoginResponseDto> login(@Valid @RequestBody AdminLoginRequestDto requestDto) {
        log.info("Admin login attempt for email={}", requestDto.getEmail());
        LoginResponseDto loginResponseDto = adminService.login(requestDto.getEmail(), requestDto.getPassword());
        return ApiResponseDto.success("Admin login successful.", loginResponseDto);
    }
}
