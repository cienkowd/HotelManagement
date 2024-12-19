package com.hotel.management.model;

import jakarta.persistence.*;

@Entity
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private int roomNumber;
    private boolean availability;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomTypes roomType;

    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = true) // Gość może być przypisany, ale nie jest wymagany
    private Guests guest;

    // Gettery i Settery
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public RoomTypes getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypes roomType) {
        this.roomType = roomType;
    }

    public Guests getGuest() {
        return guest;
    }

    public void setGuest(Guests guest) {
        this.guest = guest;
    }
}
