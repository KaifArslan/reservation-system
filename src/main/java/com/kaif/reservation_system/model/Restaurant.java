package com.kaif.reservation_system.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String address;

    // standard duration for each reservation slot in minutes (1.5 hours = 90 minutes)
    @Column(nullable = false)
    private int standardDurationMinutes = 90;

    @Column(nullable = false)
    private int totalTables;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private int maxPartySize = 10;

    public int getMaxPartySize() {
        return maxPartySize;
    }

    @Column(nullable = false)
    private LocalTime closingTime;

    @OneToMany(mappedBy="restaurant", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    public void setMaxPartySize(int maxPartySize) {
        this.maxPartySize = maxPartySize;
    }
    public int getStandardDurationMinutes() {
        return standardDurationMinutes;
    }

    public void setStandardDurationMinutes(int standardDurationMinutes) {
        this.standardDurationMinutes = standardDurationMinutes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalTables() {
        return totalTables;
    }

    public void setTotalTables(int totalTables) {
        this.totalTables = totalTables;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}
