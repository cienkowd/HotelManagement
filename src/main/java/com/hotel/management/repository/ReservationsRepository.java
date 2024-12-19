package com.hotel.management.repository;

import com.hotel.management.model.Reservations;
import com.hotel.management.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationsRepository extends JpaRepository<Reservations, Long>, JpaSpecificationExecutor<Reservations> {
    // Znajdź rezerwacje gościa po jego ID
    List<Reservations> findByGuestGuestId(Long guestId);

    @Query("SELECT r FROM Reservations r " +
            "WHERE (:roomId IS NULL OR r.room.roomId = :roomId) " +
            "AND (:guestId IS NULL OR r.guest.guestId = :guestId) " +
            "AND (:startDate IS NULL OR r.checkInDate >= :startDate) " +
            "AND (:endDate IS NULL OR r.checkOutDate <= :endDate)")
    List<Reservations> findFilteredReservations(@Param("roomId") Long roomId,
                                                @Param("guestId") Long guestId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    List<Reservations> findByRoom(Rooms room);
}
