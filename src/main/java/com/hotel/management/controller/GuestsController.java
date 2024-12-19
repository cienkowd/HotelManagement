package com.hotel.management.controller;

import com.hotel.management.model.Guests;
import com.hotel.management.model.Reservations;
import com.hotel.management.model.Rooms;
import com.hotel.management.repository.GuestsRepository;
import com.hotel.management.repository.ReservationsRepository;
import com.hotel.management.repository.RoomsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class GuestsController {

    private final GuestsRepository guestsRepository;
    private final RoomsRepository roomsRepository;
    private final ReservationsRepository reservationsRepository;

    public GuestsController(GuestsRepository guestsRepository, RoomsRepository roomsRepository, ReservationsRepository reservationsRepository) {
        this.guestsRepository = guestsRepository;
        this.roomsRepository = roomsRepository;
        this.reservationsRepository = reservationsRepository;
    }

    @GetMapping("/guests/add")
    public String showAddGuestForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Gość już istnieje w bazie!");
        }
        return "add-guest";
    }

    @PostMapping("/guests/add")
    public String addGuest(Guests guest) {
        // Sprawdź, czy gość już istnieje
        Optional<Guests> existingGuest = guestsRepository.findByNameAndSurnameAndEmail(
                guest.getName(),
                guest.getSurname(),
                guest.getEmail()
        );

        if (existingGuest.isPresent()) {
            return "redirect:/guests/add?error=true";
        }

        // Zapisz nowego gościa
        guestsRepository.save(guest);
        return "redirect:/guests/list?success=add";
    }


    @GetMapping("/guests/list")
    public String listGuests(@RequestParam(value = "success", required = false) String success, Model model) {
        model.addAttribute("guests", guestsRepository.findAll());

        if ("edit".equals(success)) {
            model.addAttribute("successMessage", "Pomyślnie edytowano dane gościa!");
        } else if ("delete".equals(success)) {
            model.addAttribute("successMessage", "Pomyślnie usunięto gościa!");
        } else if ("add".equals(success)) {
            model.addAttribute("successMessage", "Pomyślnie dodano nowego gościa!");
        }

        return "guest-list";
    }

    @GetMapping("/guests/edit/{id}")
    public String editGuest(@PathVariable Long id, Model model) {
        Guests guest = guestsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gościa o ID: " + id));
        model.addAttribute("guest", guest);
        return "edit-guest";
    }

    @PostMapping("/guests/edit/{id}")
    public String saveGuestEdit(@PathVariable Long id, Guests updatedGuest) {
        Guests existingGuest = guestsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gościa o ID: " + id));

        existingGuest.setName(updatedGuest.getName());
        existingGuest.setSurname(updatedGuest.getSurname());
        existingGuest.setEmail(updatedGuest.getEmail());
        existingGuest.setPhoneNumber(updatedGuest.getPhoneNumber());

        guestsRepository.save(existingGuest);

        return "redirect:/guests/list?success=edit";
    }

    @GetMapping("/guests/delete/{id}")
    public String confirmDelete(@PathVariable Long id, Model model) {
        Guests guest = guestsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gościa o ID: " + id));
        model.addAttribute("guest", guest);
        return "confirm-delete";
    }

    @PostMapping("/guests/delete/{id}")
    public String deleteGuest(@PathVariable Long id, Model model) {
        Guests guest = guestsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gościa o ID: " + id));

        // Sprawdź, czy gość ma przypisane pokoje
        List<Rooms> assignedRooms = roomsRepository.findByGuestGuestId(id);
        if (!assignedRooms.isEmpty()) {
            model.addAttribute("errorMessage", "Nie można usunąć gościa, ponieważ ma przypisane pokoje.");
            model.addAttribute("guests", guestsRepository.findAll());
            return "guest-list";
        }

        // Sprawdź, czy gość ma rezerwacje
        List<Reservations> assignedReservations = reservationsRepository.findByGuestGuestId(id);
        if (!assignedReservations.isEmpty()) {
            model.addAttribute("errorMessage", "Nie można usunąć gościa, ponieważ ma aktywne rezerwacje.");
            model.addAttribute("guests", guestsRepository.findAll());
            return "guest-list";
        }

        guestsRepository.deleteById(id);
        return "redirect:/guests/list?success=delete";
    }

}
