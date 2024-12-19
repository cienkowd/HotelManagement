package com.hotel.management.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String reservationStatus;

    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private Guests guest;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms room;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public Guests getGuest() {
        return guest;
    }

    public void setGuest(Guests guest) {
        this.guest = guest;
    }

    public Rooms getRoom() {
        return room;
    }

    public void setRoom(Rooms room) {
        this.room = room;
    }
}
