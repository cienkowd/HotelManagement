package com.hotel.management.service;

import com.hotel.management.model.Reports;
import com.hotel.management.repository.ReportsRepository;
import com.hotel.management.repository.ReservationsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
    private final ReportsRepository reportsRepository;
    private final ReservationsRepository reservationsRepository;

    public ReportService(ReportsRepository reportsRepository, ReservationsRepository reservationsRepository) {
        this.reportsRepository = reportsRepository;
        this.reservationsRepository = reservationsRepository;
    }

    // Generuj raport o zajętości pokoi
    public Reports generateOccupancyReport(String generatedBy) {
        List<?> occupancyData = reservationsRepository.findAll(); // Możesz dodać własną logikę zapytań
        Reports report = new Reports();
        report.setReportDate(LocalDate.now());
        report.setDataRange("All Time");
        report.setGeneratedBy(generatedBy);

        return reportsRepository.save(report);
    }

    // Pobierz wszystkie raporty
    public List<Reports> getAllReports() {
        return reportsRepository.findAll();
    }
}
