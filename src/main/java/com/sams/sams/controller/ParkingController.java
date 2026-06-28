package com.sams.sams.controller;

import com.sams.sams.model.ParkingSlot;
import com.sams.sams.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ParkingController {

    @Autowired private ParkingRepository parkingRepository;

    @GetMapping("/parking")
    public String parking(Model model) {
        List<ParkingSlot> slots = parkingRepository.findAll();
        model.addAttribute("slots", slots);
        model.addAttribute("totalCount", slots.size());
        model.addAttribute("availableCount", parkingRepository.countByStatus("Available"));
        model.addAttribute("occupiedCount", parkingRepository.countByStatus("Occupied"));
        model.addAttribute("reservedCount", parkingRepository.countByStatus("Reserved"));
        model.addAttribute("activePage", "parking");
        return "parking";
    }

    @PostMapping("/parking/add")
    public String addSlot(@ModelAttribute ParkingSlot slot) {
        if (slot.getStatus() == null || slot.getStatus().isEmpty()) {
            slot.setStatus("Available");
        }
        parkingRepository.save(slot);
        return "redirect:/parking";
    }

    @PostMapping("/parking/edit")
    public String editSlot(@ModelAttribute ParkingSlot slot) {
        ParkingSlot existing = parkingRepository.findById(slot.getId()).orElse(null);
        if (existing != null) {
            existing.setSlotNumber(slot.getSlotNumber());
            existing.setSlotType(slot.getSlotType());
            existing.setStatus(slot.getStatus());
            existing.setAssignedTo(slot.getAssignedTo());
            existing.setApartmentNumber(slot.getApartmentNumber());
            existing.setVehicleNumber(slot.getVehicleNumber());
            existing.setVehicleType(slot.getVehicleType());
            parkingRepository.save(existing);
        }
        return "redirect:/parking";
    }

    @PostMapping("/parking/delete/{id}")
    public String deleteSlot(@PathVariable Long id) {
        parkingRepository.deleteById(id);
        return "redirect:/parking";
    }
}