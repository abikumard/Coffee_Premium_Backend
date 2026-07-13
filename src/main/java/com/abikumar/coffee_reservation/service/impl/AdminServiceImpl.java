package com.abikumar.coffee_reservation.service.impl;

import com.abikumar.coffee_reservation.constant.AppConstants;
import com.abikumar.coffee_reservation.dao.AdminDao;
import com.abikumar.coffee_reservation.dto.LoginResponseDto;
import com.abikumar.coffee_reservation.entity.Admin;
import com.abikumar.coffee_reservation.entity.AuthToken;
import com.abikumar.coffee_reservation.exception.UnauthorizedException;
import com.abikumar.coffee_reservation.service.AdminService;
import com.abikumar.coffee_reservation.service.AuthTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final AdminDao adminDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;

    @Value("${admin.default.email}")
    private String defaultAdminEmail;

    @Value("${admin.default.password}")
    private String defaultAdminPassword;

    public AdminServiceImpl(AdminDao adminDao, PasswordEncoder passwordEncoder, AuthTokenService authTokenService) {
        this.adminDao = adminDao;
        this.passwordEncoder = passwordEncoder;
        this.authTokenService = authTokenService;
    }

    @Override
    public LoginResponseDto login(String email, String rawPassword) {
        Admin admin = adminDao.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid admin email or password"));

        if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
            log.warn("Failed admin login attempt for email={}", email);
            throw new UnauthorizedException("Invalid admin email or password");
        }

        AuthToken authToken = authTokenService.issueToken(email, AppConstants.ROLE_ADMIN);
        log.info("Admin login successful for email={}", email);
        return new LoginResponseDto(authToken.getToken(), email, AppConstants.ROLE_ADMIN);
    }

    @Override
    public void seedDefaultAdminIfMissing() {
        if (adminDao.countAdmins() > 0) {
            log.info("Admin account already present. Skipping seed step.");
            return;
        }
        Admin admin = new Admin();
        admin.setEmail(defaultAdminEmail);
        admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
        admin.setCreatedDate(LocalDateTime.now());
        adminDao.insertAdmin(admin);
        log.info("Default admin account created for email={}", defaultAdminEmail);
    }
}
