package com.hotel.management.repository;

import com.hotel.management.model.RoomTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypesRepository extends JpaRepository<RoomTypes, Long> {
    // Znajdź typ pokoju po nazwie
    RoomTypes findByRoomTypeName(String roomTypeName);
}
