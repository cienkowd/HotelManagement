package com.hotel.management.controller;

import com.hotel.management.model.RoomTypes;
import com.hotel.management.repository.RoomTypesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoomTypesController {

    private final RoomTypesRepository roomTypesRepository;

    public RoomTypesController(RoomTypesRepository roomTypesRepository) {
        this.roomTypesRepository = roomTypesRepository;
    }

    @GetMapping("/roomtypes/add")
    public String showAddRoomTypeForm() {
        return "add-roomtype";
    }

    @PostMapping("/roomtypes/add")
    public String addRoomType(@RequestParam String roomTypeName,
                              @RequestParam String description) {
        RoomTypes roomType = new RoomTypes();
        roomType.setRoomTypeName(roomTypeName);
        roomType.setDescription(description);

        roomTypesRepository.save(roomType);

        return "redirect:/rooms/manage?success=addRoomType";
    }
}

