package com.hotel.management.controller;

import org.springframework.ui.Model;
import com.hotel.management.model.Reservations;
import com.hotel.management.repository.GuestsRepository;
import com.hotel.management.repository.ReservationsRepository;
import com.hotel.management.repository.RoomsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class ReportsController {

    private final ReservationsRepository reservationsRepository;
    private final GuestsRepository guestsRepository;
    private final RoomsRepository roomsRepository;

    public ReportsController(ReservationsRepository reservationsRepository,
                             GuestsRepository guestsRepository,
                             RoomsRepository roomsRepository) {
        this.reservationsRepository = reservationsRepository;
        this.guestsRepository = guestsRepository;
        this.roomsRepository = roomsRepository;
    }

    @GetMapping("/reports")
    public String showReportForm(Model model) {
        model.addAttribute("rooms", roomsRepository.findAll());
        model.addAttribute("guests", guestsRepository.findAll());
        return "generate-report";
    }

    @GetMapping("/reports/generate")
    public String generateReport(@RequestParam(required = false) Long roomId,
                                 @RequestParam(required = false) Long guestId,
                                 @RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate,
                                 Model model) {
        List<Reservations> reservations;

        // Walidacja dat i parsowanie tylko gdy parametry nie są puste
        LocalDate start = null;
        LocalDate end = null;
        try {
            if (startDate != null && !startDate.isEmpty()) {
                start = LocalDate.parse(startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                end = LocalDate.parse(endDate);
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("errorMessage", "Nieprawidłowy format daty. Proszę wprowadzić daty w formacie YYYY-MM-DD.");
            return "generate-report"; // Wróć do formularza z komunikatem błędu
        }

        // Filtruj rezerwacje tylko jeśli parametry są podane
        if (roomId != null || guestId != null || (start != null && end != null)) {
            reservations = reservationsRepository.findFilteredReservations(
                    roomId,
                    guestId,
                    start,
                    end
            );
        } else {
            reservations = reservationsRepository.findAll();
        }

        model.addAttribute("reservations", reservations);
        return "report-results";
    }
}

