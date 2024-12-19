package com.hotel.management.repository;

import com.hotel.management.model.Reports;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportsRepository extends JpaRepository<Reports, Long> {
    // Znajdź raporty wygenerowane przez użytkownika
    List<Reports> findByGeneratedBy(String generatedBy);

    // Znajdź raporty w określonym przedziale dat
    List<Reports> findByReportDateBetween(LocalDate startDate, LocalDate endDate);
}
