package com.kaif.reservation_system.repository;

import com.kaif.reservation_system.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRestaurantId(Long id);
    List<Reservation> findByRestaurantIdAndReservationDate(int restaurantId, LocalDate reservationDate);
//    List<Reservation> findByRestaurantIdAndReservationDateGreaterThanEqualAndReservationTimeGreaterThanEqualOrderByReservationDateAscReservationTimeAsc(
//            int restaurantId, LocalDate reservationDate, LocalTime reservationTime
//    );


}
