package com.kaif.reservation_system.repository;

import com.kaif.reservation_system.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

}
