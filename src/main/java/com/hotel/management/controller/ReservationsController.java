package com.hotel.management.controller;

import com.hotel.management.model.Guests;
import com.hotel.management.model.Reservations;
import com.hotel.management.model.Rooms;
import com.hotel.management.repository.GuestsRepository;
import com.hotel.management.repository.ReservationsRepository;
import com.hotel.management.repository.RoomsRepository;
import com.hotel.management.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservationsController {

    private final ReservationsRepository reservationsRepository;
    private final GuestsRepository guestsRepository;
    private final RoomsRepository roomsRepository;

    private final ReservationService reservationService;

    public ReservationsController(ReservationsRepository reservationsRepository,
                                  GuestsRepository guestsRepository,
                                  RoomsRepository roomsRepository, ReservationService reservationService) {
        this.reservationsRepository = reservationsRepository;
        this.guestsRepository = guestsRepository;
        this.roomsRepository = roomsRepository;
        this.reservationService = reservationService;
    }

    // Formularz tworzenia rezerwacji
    @GetMapping("/reservations/add")
    public String showAddReservationForm(Model model) {
        model.addAttribute("guests", guestsRepository.findAll());
        model.addAttribute("availableRooms", roomsRepository.findByAvailability(true)); // Tylko dostępne pokoje
        return "add-reservation";
    }


    @PostMapping("/reservations/add")
    public String addReservation(@RequestParam Long guestId,
                                 @RequestParam Long roomId,
                                 @RequestParam String checkInDate,
                                 @RequestParam String checkOutDate,
                                 @RequestParam String reservationStatus) {
        Guests guest = guestsRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gościa o ID: " + guestId));
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pokoju o ID: " + roomId));

        Reservations reservation = new Reservations();
        reservation.setGuest(guest);
        reservation.setRoom(room);
        reservation.setCheckInDate(LocalDate.parse(checkInDate));
        reservation.setCheckOutDate(LocalDate.parse(checkOutDate));
        reservation.setReservationStatus(reservationStatus);
        
        room.setGuest(guest);

        reservationsRepository.save(reservation);
        return "redirect:/reservations/manage?success=add";
    }



    // Wyświetlanie listy wszystkich rezerwacji (zarządzanie rezerwacjami)
    @GetMapping("/reservations/manage")
    public String manageReservations(
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "guestId", required = false) Long guestId,
            @RequestParam(value = "roomNumber", required = false) Integer roomNumber,
            @RequestParam(value = "checkInDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(value = "checkOutDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(value = "reservationStatus", required = false) String reservationStatus,
            Model model) {

        // Filtruj rezerwacje na podstawie podanych parametrów
        List<Reservations> reservations = reservationService.filterReservations(
                guestId, roomNumber, checkInDate, checkOutDate, reservationStatus);
        model.addAttribute("reservations", reservations);

        // Dodaj listę gości i pokoi do formularza filtrowania
        model.addAttribute("guests", guestsRepository.findAll());
        model.addAttribute("rooms", roomsRepository.findAll());

        // Jeśli guestId istnieje, znajdź gościa
        if (guestId != null) {
            guestsRepository.findById(guestId).ifPresent(guest ->
                    model.addAttribute("filteredGuestName", guest.getName() + " " + guest.getSurname()));
        }

        // Obsługa komunikatów sukcesu
        if ("delete".equals(success)) {
            model.addAttribute("successMessage", "Rezerwacja została pomyślnie usunięta!");
        } else if ("add".equals(success)) {
            model.addAttribute("successMessage", "Rezerwacja została pomyślnie dodana!");
        } else if ("update".equals(success)) {
            model.addAttribute("successMessage", "Rezerwacja została pomyślnie zaktualizowana!");
        }

        // Obsługa komunikatów błędów
        if ("notfound".equals(error)) {
            model.addAttribute("errorMessage", "Nie znaleziono rezerwacji do usunięcia!");
        } else if ("conflict".equals(error)) {
            model.addAttribute("errorMessage", "Wystąpił konflikt przy aktualizacji rezerwacji!");
        }

        return "manage-reservations"; // Widok zarządzania rezerwacjami
    }


    // Formularz edycji istniejącej rezerwacji
    @GetMapping("/reservations/edit/{id}")
    public String showEditReservationForm(@PathVariable Long id, Model model) {
        Reservations reservation = reservationsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono rezerwacji o ID: " + id));

        model.addAttribute("reservation", reservation);
        model.addAttribute("guests", guestsRepository.findAll());
        return "edit-reservation";
    }

    // Przetwarzanie edycji rezerwacji
    @PostMapping("/reservations/edit/{id}")
    public String updateReservation(@PathVariable Long id,
                                    @RequestParam Long guestId,
                                    @RequestParam String checkInDate,
                                    @RequestParam String checkOutDate,
                                    @RequestParam String reservationStatus) {
        Reservations reservation = reservationsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono rezerwacji o ID: " + id));

        Guests guest = guestsRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gościa o ID: " + guestId));

        reservation.setGuest(guest);
        reservation.setCheckInDate(LocalDate.parse(checkInDate));
        reservation.setCheckOutDate(LocalDate.parse(checkOutDate));
        reservation.setReservationStatus(reservationStatus);

        reservationsRepository.save(reservation);

        return "redirect:/reservations/manage?success=update";
    }


    // Usuwanie rezerwacji
    @GetMapping("/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        Optional<Reservations> reservation = reservationsRepository.findById(id);
        if (reservation.isPresent()) {
            Rooms room = reservation.get().getRoom();
            room.setAvailability(true);
            room.setGuest(null);
            roomsRepository.save(room);

            reservationsRepository.deleteById(id);
            return "redirect:/reservations/manage?success=delete"; // Przekierowanie z komunikatem sukcesu
        } else {
            return "redirect:/reservations/manage?error=notfound"; // Przekierowanie z komunikatem błędu
        }
    }

}
