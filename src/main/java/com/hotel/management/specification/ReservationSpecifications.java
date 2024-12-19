package com.hotel.management.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import com.hotel.management.model.Reservations;


import java.time.LocalDate;

public class ReservationSpecifications {

    public static Specification<Reservations> withGuestId(Long guestId) {
        return (Root<Reservations> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                guestId == null ? null : cb.equal(root.get("guest").get("guestId"), guestId);
    }

    public static Specification<Reservations> withRoomNumber(Integer roomNumber) {
        return (Root<Reservations> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                roomNumber == null ? null : cb.equal(root.get("room").get("roomNumber"), roomNumber);
    }

    public static Specification<Reservations> withCheckInDate(LocalDate checkInDate) {
        return (Root<Reservations> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                checkInDate == null ? null : cb.greaterThanOrEqualTo(root.get("checkInDate"), checkInDate);
    }

    public static Specification<Reservations> withCheckOutDate(LocalDate checkOutDate) {
        return (Root<Reservations> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                checkOutDate == null ? null : cb.lessThanOrEqualTo(root.get("checkOutDate"), checkOutDate);
    }

    public static Specification<Reservations> withStatus(String status) {
        return (Root<Reservations> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                status == null || status.isEmpty() ? null : cb.equal(root.get("reservationStatus"), status);
    }
}
