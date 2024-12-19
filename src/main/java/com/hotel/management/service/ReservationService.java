package com.hotel.management.service;

import com.hotel.management.model.Guests;
import com.hotel.management.model.Reservations;
import com.hotel.management.model.Rooms;
import com.hotel.management.repository.GuestsRepository;
import com.hotel.management.repository.ReservationsRepository;
import com.hotel.management.repository.RoomsRepository;
import com.hotel.management.specification.ReservationSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationsRepository reservationsRepository;
    private final RoomsRepository roomsRepository;
    private final GuestsRepository guestsRepository;


    public ReservationService(ReservationsRepository reservationsRepository,
                              RoomsRepository roomsRepository,
                              GuestsRepository guestsRepository) {
        this.reservationsRepository = reservationsRepository;
        this.roomsRepository = roomsRepository;
        this.guestsRepository = guestsRepository;
    }

    // Utwórz rezerwację
    public Reservations createReservation(Long guestId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        // Znajdź pokój po jego ID
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Sprawdź dostępność pokoju
        if (!room.isAvailability()) {
            throw new RuntimeException("Room is not available");
        }

        // Znajdź gościa po jego ID
        Guests guest = guestsRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        // Utwórz nową rezerwację
        Reservations reservation = new Reservations();
        reservation.setGuest(guest);  // Ustawienie obiektu Guests
        reservation.setRoom(room);   // Ustawienie obiektu Rooms
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setReservationStatus("Confirmed");

        // Aktualizuj dostępność pokoju
        room.setAvailability(false);
        roomsRepository.save(room);

        return reservationsRepository.save(reservation);
    }

    public List<Reservations> filterReservations(
            Long guestId, Integer roomNumber, LocalDate checkInDate, LocalDate checkOutDate, String reservationStatus) {

        return reservationsRepository.findAll(Specification.where(
                ReservationSpecifications.withGuestId(guestId)
                        .and(ReservationSpecifications.withRoomNumber(roomNumber))
                        .and(ReservationSpecifications.withCheckInDate(checkInDate))
                        .and(ReservationSpecifications.withCheckOutDate(checkOutDate))
                        .and(ReservationSpecifications.withStatus(reservationStatus))
        ));
    }


}
