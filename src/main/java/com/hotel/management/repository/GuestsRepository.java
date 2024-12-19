package com.hotel.management.repository;

import com.hotel.management.model.Guests;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestsRepository extends JpaRepository<Guests, Long> {
    // Znajdź gości po nazwisku
    List<Guests> findBySurname(String surname);

    // Znajdź gościa po imieniu i nazwisku
    Optional<Guests> findByNameAndSurnameAndEmail(String name, String surname, String email);
}