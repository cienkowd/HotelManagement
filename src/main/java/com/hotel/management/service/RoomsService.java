package com.hotel.management.service;

import com.hotel.management.model.Rooms;
import com.hotel.management.repository.RoomsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomsService {
    private final RoomsRepository roomsRepository;

    public RoomsService(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    // Sprawdź dostępne pokoje
    public List<Rooms> getAvailableRooms() {
        return roomsRepository.findByAvailability(true);
    }

    // Sprawdź dostępne pokoje określonego typu
    public List<Rooms> getAvailableRoomsByType(String roomTypeName) {
        return roomsRepository.findByRoomTypeRoomTypeName(roomTypeName);
    }

    // Zaktualizuj status dostępności pokoju
    public void updateRoomAvailability(Long roomId, boolean availability) {
        Rooms room = roomsRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.setAvailability(availability);
        roomsRepository.save(room);
    }
}
