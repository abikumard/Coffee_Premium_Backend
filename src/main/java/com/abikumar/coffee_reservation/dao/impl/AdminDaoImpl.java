package com.abikumar.coffee_reservation.dao.impl;

import com.abikumar.coffee_reservation.dao.AdminDao;
import com.abikumar.coffee_reservation.entity.Admin;
import com.abikumar.coffee_reservation.rowmapper.AdminRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class AdminDaoImpl implements AdminDao {

    private static final Logger log = LoggerFactory.getLogger(AdminDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final AdminRowMapper adminRowMapper = new AdminRowMapper();

    public AdminDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        String sql = "SELECT * FROM admin WHERE email = ?";
        try {
            Admin admin = jdbcTemplate.queryForObject(sql, adminRowMapper, email);
            return Optional.ofNullable(admin);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public int countAdmins() {
        String sql = "SELECT COUNT(*) FROM admin";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public void insertAdmin(Admin admin) {
        String sql = "INSERT INTO admin (email, password, created_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, admin.getEmail(), admin.getPassword(), Timestamp.valueOf(admin.getCreatedDate()));
        log.info("Seeded default admin account for email={}", admin.getEmail());
    }
}
