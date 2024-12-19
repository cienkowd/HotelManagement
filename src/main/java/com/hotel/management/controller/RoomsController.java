package com.hotel.management.controller;

import com.hotel.management.model.Guests;
import com.hotel.management.model.Reservations;
import com.hotel.management.model.RoomTypes;
import com.hotel.management.model.Rooms;
import com.hotel.management.repository.GuestsRepository;
import com.hotel.management.repository.ReservationsRepository;
import com.hotel.management.repository.RoomTypesRepository;
import com.hotel.management.repository.RoomsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RoomsController {

    private final RoomsRepository roomsRepository;
    private final GuestsRepository guestsRepository;
    private final RoomTypesRepository roomTypesRepository;
    private final ReservationsRepository reservationsRepository;

    public RoomsController(RoomsRepository roomsRepository, GuestsRepository guestsRepository, RoomTypesRepository roomTypesRepository, ReservationsRepository reservationsRepository) {
        this.roomsRepository = roomsRepository;
        this.guestsRepository = guestsRepository;
        this.roomTypesRepository = roomTypesRepository;
        this.reservationsRepository = reservationsRepository;
    }

    // Wyświetlanie listy pokoi
    @GetMapping("/rooms/manage")
    public String showManageRooms(@RequestParam(value = "success", required = false) String success, Model model) {
        model.addAttribute("rooms", roomsRepository.findAll());

        if ("assign".equals(success)) {
            model.addAttribute("successMessage", "Gość został pomyślnie przypisany do pokoju!");
        } else if ("unassign".equals(success)) {
            model.addAttribute("successMessage", "Przypisanie gościa zostało usunięte!");
        } else if ("add".equals(success)) {
            model.addAttribute("successMessage", "Nowy pokój został pomyślnie dodany!");
        } else if ("update".equals(success)) {
            model.addAttribute("successMessage", "Pokój został pomyślnie zaktualizowany!");
        }

        return "manage-rooms";
    }


    // Formularz przypisywania gościa do pokoju
    @GetMapping("/rooms/assign/{id}")
    public String showAssignGuestForm(@PathVariable Long id, Model model) {
        Rooms room = roomsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pokoju o ID: " + id));
        model.addAttribute("room", room);
        model.addAttribute("guests", guestsRepository.findAll());
        return "assign-guest";
    }

    // Przypisywanie gościa do pokoju
    @PostMapping("/rooms/assign/{id}")
    public String assignGuestToRoom(@PathVariable Long id, @RequestParam Long guestId, Model model) {
        Rooms room = roomsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pokoju o ID: " + id));
        Guests guest = guestsRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gościa o ID: " + guestId));

        // Sprawdź, czy pokój jest dostępny
        if (!room.isAvailability()) {
            model.addAttribute("errorMessage", "Pokój jest już zajęty!");
            model.addAttribute("room", room);
            model.addAttribute("guests", guestsRepository.findAll());
            return "assign-guest";
        }

        room.setGuest(guest); // Przypisz gościa do pokoju
        room.setAvailability(false); // Oznacz pokój jako zajęty
        roomsRepository.save(room);
        return "redirect:/rooms/manage?success=assign";
    }

    @GetMapping("/rooms/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("roomTypes", roomTypesRepository.findAll()); // Pobranie typów pokoi
        return "add-room";
    }

    @PostMapping("/rooms/add")
    public String addRoom(@RequestParam int roomNumber, @RequestParam Long roomTypeId) {
        RoomTypes roomType = roomTypesRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono typu pokoju o ID: " + roomTypeId));

        Rooms room = new Rooms();
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setAvailability(true);
        room.setGuest(null);

        roomsRepository.save(room);
        return "redirect:/rooms/manage?success=add";
    }


    @GetMapping("/rooms/unassign/{id}")
    public String unassignGuestFromRoom(@PathVariable Long id) {
        // Znajdź pokój w bazie
        Rooms room = roomsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pokoju o ID: " + id));

        // Usuń przypisanie gościa i ustaw dostępność
        room.setGuest(null);
        room.setAvailability(true);
        roomsRepository.save(room);

        // Znajdź wszystkie rezerwacje powiązane z tym pokojem
        List<Reservations> reservations = reservationsRepository.findByRoom(room);

        // Zaktualizuj rezerwacje, ustawiając brak pokoju
        for (Reservations reservation : reservations) {
            reservation.setRoom(null);
            reservationsRepository.save(reservation);
        }

        return "redirect:/rooms/manage?success=unassign";
    }

    @GetMapping("/rooms/update/{id}")
    public String showUpdateRoomForm(@PathVariable Long id, Model model) {
        Rooms room = roomsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pokoju o ID: " + id));
        model.addAttribute("room", room);
        model.addAttribute("roomTypes", roomTypesRepository.findAll()); // Pobierz dostępne typy pokoi
        return "update-room"; // Wyświetl widok formularza
    }

    @PostMapping("/rooms/update/{id}")
    public String updateRoom(@PathVariable Long id, @RequestParam int roomNumber, @RequestParam Long roomTypeId) {
        Rooms room = roomsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pokoju o ID: " + id));
        RoomTypes roomType = roomTypesRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono typu pokoju o ID: " + roomTypeId));

        // Aktualizacja danych pokoju
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);

        roomsRepository.save(room);
        return "redirect:/rooms/manage?success=update";
    }


}
