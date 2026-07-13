package com.abikumar.coffee_reservation.config;

import com.abikumar.coffee_reservation.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * On application startup, makes sure exactly one admin account exists in MySQL,
 * seeded from the credentials configured in application.properties
 * (admin.default.email / admin.default.password). No admin sign-up page exists.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final AdminService adminService;

    public AdminSeeder(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) {
        log.info("Checking whether a default admin account needs to be seeded...");
        adminService.seedDefaultAdminIfMissing();
    }
}
