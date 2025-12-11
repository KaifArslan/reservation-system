package com.kaif.reservation_system.service;

import com.kaif.reservation_system.model.Reservation;

import com.kaif.reservation_system.model.Restaurant;
import com.kaif.reservation_system.repository.ReservationRepository;
import com.kaif.reservation_system.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class ReservationService {
    private final ReservationRepository reserveRepo;
    private final RestaurantRepository restaurantRepo;

    public ReservationService(ReservationRepository rp, RestaurantRepository restaurantRepo){
        this.reserveRepo = rp;
        this.restaurantRepo = restaurantRepo;
    }

    public Reservation createReservation(Reservation rs){
        // Validate availability before creating
        Restaurant restaurant = restaurantRepo.findById(rs.getRestaurant().getId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Validate party size
        if (rs.getPartySize() > restaurant.getMaxPartySize()) {
            throw new RuntimeException("Party size exceeds maximum of " + restaurant.getMaxPartySize() +
                    ". Please contact restaurant for large groups.");
        }
        if (rs.getPartySize() < 1) {
            throw new RuntimeException("Party size must be at least 1");
        }
        if (!isAvailable(rs.getRestaurant().getId(), rs.getReservationDate(), rs.getStartTime())) {
            throw new RuntimeException("Time slot not available");
        }

        if(rs.getEndTime() == null) {

            int duration = restaurant.getStandardDurationMinutes();
            rs.setEndTime(rs.getStartTime().plusMinutes(duration));
        }

        rs.setStatus(Reservation.Status.CONFIRMED);
        return reserveRepo.save(rs);
    }

    public Reservation getReservationId(long id){
        return reserveRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public Reservation updateReservation(Reservation reNew) {
        Reservation existing = getReservationId(reNew.getId());
        // Add after fetching existing reservation
        Restaurant restaurant = restaurantRepo.findById(existing.getRestaurant().getId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (reNew.getPartySize() > restaurant.getMaxPartySize()) {
            throw new RuntimeException("Party size exceeds maximum of " + restaurant.getMaxPartySize());
        }
        if (reNew.getPartySize() < 1) {
            throw new RuntimeException("Party size must be at least 1");
        }
        // Validate availability for new time if time is being changed
        if (!existing.getStartTime().equals(reNew.getStartTime()) ||
                !existing.getReservationDate().equals(reNew.getReservationDate())) {
            if (!isAvailable(existing.getRestaurant().getId(), reNew.getReservationDate(), reNew.getStartTime())) {
                throw new RuntimeException("New time slot not available");
            }
        }

        existing.setCustomerName(reNew.getCustomerName());
        existing.setCustomerPhone(reNew.getCustomerPhone());
        existing.setStartTime(reNew.getStartTime());
        existing.setReservationDate(reNew.getReservationDate());
        existing.setPartySize(reNew.getPartySize());

        if (reNew.getEndTime() == null) {
            int duration = restaurant.getStandardDurationMinutes();
            existing.setEndTime(reNew.getStartTime().plusMinutes(duration));
        } else {
            existing.setEndTime(reNew.getEndTime());
        }

        existing.onUpdate();

        return reserveRepo.save(existing);
    }

    public List<Reservation> getAllReservations(){
        return reserveRepo.findAll();
    }

    public void deleteReservation(long id){
        reserveRepo.deleteById(id);
    }

    public List<Reservation> getReservationsByRestaurantId(Long restaurantId){
        return reserveRepo.findByRestaurantId(restaurantId);
    }

   //Keeping the 15 minutes standard for reservations
    public List<LocalTime> getAvailableTimeSlots(int restaurantId, LocalDate date) {
        Restaurant restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        LocalTime open = restaurant.getOpeningTime();
        LocalTime close = restaurant.getClosingTime();
        int duration = restaurant.getStandardDurationMinutes(); // 90

        List<LocalTime> availableSlots = new ArrayList<>();

        // Generate slots every 15 minutes (flexible booking)
        LocalTime currentSlot = open;

        while (!currentSlot.plusMinutes(duration).isAfter(close)) {
            // Check if this specific time is available
            if (isAvailable(restaurantId, date, currentSlot)) {
                availableSlots.add(currentSlot);
            }

            // Move to next 15-minute interval
            currentSlot = currentSlot.plusMinutes(15);
        }

        return availableSlots;
    }


    public boolean isAvailable(int restaurantId, LocalDate date, LocalTime startTime) {
        Restaurant restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        int duration = restaurant.getStandardDurationMinutes();
        LocalTime endTime = startTime.plusMinutes(duration);

        // Check 1: Within working hours
        if (startTime.isBefore(restaurant.getOpeningTime())) {
            return false;
        }
        if (endTime.isAfter(restaurant.getClosingTime())) {
            return false;
        }

        int totalTables = restaurant.getTotalTables();

        // Check 2: Get all reservations for the same date
        List<Reservation> reservations =
                reserveRepo.findByRestaurantIdAndReservationDate(restaurantId, date);

        int overlappingCount = 0;

        // Check 3: Count overlapping reservations
        for (Reservation r : reservations) {
            // Skip cancelled reservations
            if (r.getStatus() == Reservation.Status.CANCELLED) {
                continue;
            }

            LocalTime rStart = r.getStartTime();
            LocalTime rEnd = r.getEndTime();

            // Overlap condition: A.start < B.end AND A.end > B.start
            boolean overlaps = startTime.isBefore(rEnd) && endTime.isAfter(rStart);

            if (overlaps) {
                overlappingCount++;
            }
        }

        // Available if overlapping reservations < total tables
        return overlappingCount < totalTables;
    }

    public void cancelReservation(long id){
        Reservation res = getReservationId(id);
        res.setStatus(Reservation.Status.CANCELLED);
        res.onUpdate(); // Update the updatedAt timestamp
        reserveRepo.save(res); // Save the changes
    }

//    public int counter(int restaurantId){
//
//        reserveRepo.countByRestaurantId(restaurantId);
//    }
}