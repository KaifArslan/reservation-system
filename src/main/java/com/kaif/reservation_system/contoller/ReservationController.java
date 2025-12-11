package com.kaif.reservation_system.contoller;


import com.kaif.reservation_system.service.ReservationService;
import com.kaif.reservation_system.model.Reservation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService rs) {
        this.reservationService = rs;
    }

    // CREATE
    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

    // READ ONE
    @GetMapping("/{id}")
    public Reservation getReservation(@PathVariable long id) {
        return reservationService.getReservationId(id);
    }

    // READ ALL
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // UPDATE
    @PutMapping("/{id}")
    public Reservation updateReservation(
            @PathVariable long id,
            @RequestBody Reservation reservation
    ) {
        reservation.setId(id); // ensure ID is correct
        return reservationService.updateReservation(reservation);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable long id) {
        reservationService.deleteReservation(id);
    }

    // GET ALL RESERVATIONS OF A RESTAURANT
    @GetMapping("/restaurant/{restaurantId}")
    public List<Reservation> getByRestaurant(@PathVariable int restaurantId) {
        return reservationService.getReservationsByRestaurantId((long) restaurantId);
    }

    // CHECK AVAILABILITY
    @GetMapping("/availability")
    public boolean checkAvailability(
            @RequestParam int restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time
    ) {
        return reservationService.isAvailable(restaurantId, date, time);
    }

    // GET AVAILABLE TIME SLOTS
    @GetMapping("/available-slots")
    public List<LocalTime> getAvailableSlots(
            @RequestParam int restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return reservationService.getAvailableTimeSlots(restaurantId, date);
    }
    @PatchMapping("/{id}/cancel")
    public Reservation cancelReservation(@PathVariable long id) {
        reservationService.cancelReservation(id);
        return reservationService.getReservationId(id);
    }
}
