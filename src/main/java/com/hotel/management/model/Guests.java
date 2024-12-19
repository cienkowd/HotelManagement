package com.hotel.management.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Guests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    @OneToMany(mappedBy = "guest")
    private List<Rooms> rooms; // Lista pokoi przypisanych do gościa

    // Gettery i Settery
    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Rooms> getRooms() {
        return rooms;
    }

    public void setRooms(List<Rooms> rooms) {
        this.rooms = rooms;
    }

}
