package com.abikumar.coffee_reservation.dao;

import com.abikumar.coffee_reservation.entity.Admin;

import java.util.Optional;

public interface AdminDao {

    Optional<Admin> findByEmail(String email);

    int countAdmins();

    void insertAdmin(Admin admin);
}
