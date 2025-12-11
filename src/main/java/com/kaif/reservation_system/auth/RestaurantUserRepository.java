package com.kaif.reservation_system.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantUserRepository extends JpaRepository<RestaurantUser, Long> {
    Optional<RestaurantUser> findByEmail(String email);
}
