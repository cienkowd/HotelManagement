package com.hotel.management.repository;

import com.hotel.management.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    // Znajdź pokoje dostępne
    List<Rooms> findByAvailability(boolean availability);

    // Znajdź pokoje po typie pokoju
    List<Rooms> findByRoomTypeRoomTypeName(String roomTypeName);

    // Znajdź pokoje przypisane do danego gościa
    List<Rooms> findByGuestGuestId(Long guestId);

}
