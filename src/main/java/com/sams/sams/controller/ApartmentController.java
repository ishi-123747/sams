package com.sams.sams.controller;

import com.sams.sams.model.Apartment;
import com.sams.sams.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ApartmentController {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @GetMapping("/apartments")
    public String apartments(Model model) {
        try {
            List<Apartment> apartments = apartmentRepository.findAll();
            long occupied    = apartments.stream().filter(a -> "Occupied".equalsIgnoreCase(a.getStatus())).count();
            long vacant      = apartments.stream().filter(a -> "Vacant".equalsIgnoreCase(a.getStatus())).count();
            long maintenance = apartments.stream().filter(a -> "Maintenance".equalsIgnoreCase(a.getStatus())).count();
            model.addAttribute("apartments",   apartments);
            model.addAttribute("total",        apartments.size());
            model.addAttribute("occupied",     occupied);
            model.addAttribute("vacant",       vacant);
            model.addAttribute("maintenance",  maintenance);
        } catch (Exception e) {
            model.addAttribute("apartments",  java.util.Collections.emptyList());
            model.addAttribute("total",       0);
            model.addAttribute("occupied",    0);
            model.addAttribute("vacant",      0);
            model.addAttribute("maintenance", 0);
        }
        return "apartments";
    }

    @PostMapping("/apartments/add")
    public String addApartment(
            @RequestParam String apartmentNumber,
            @RequestParam String apartmentName,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, defaultValue = "0") int rooms,
            @RequestParam(required = false, defaultValue = "0") double rent,
            @RequestParam(required = false, defaultValue = "0") double deposit,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tenantName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Apartment a = new Apartment();
        a.setApartmentNumber(apartmentNumber);
        a.setApartmentName(apartmentName);
        a.setLocation(location);
        a.setRooms(rooms);
        a.setRent(rent);
        a.setDeposit(deposit);
        a.setStatus(status != null ? status : "Vacant");
        a.setTenantName(tenantName);
        a.setImageUrl(saveImage(imageFile));
        apartmentRepository.save(a);
        return "redirect:/apartments";
    }

    @PostMapping("/apartments/edit")
    public String editApartment(
            @RequestParam Long id,
            @RequestParam String apartmentNumber,
            @RequestParam String apartmentName,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, defaultValue = "0") int rooms,
            @RequestParam(required = false, defaultValue = "0") double rent,
            @RequestParam(required = false, defaultValue = "0") double deposit,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tenantName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Apartment a = apartmentRepository.findById(id).orElse(new Apartment());
        a.setId(id);
        a.setApartmentNumber(apartmentNumber);
        a.setApartmentName(apartmentName);
        a.setLocation(location);
        a.setRooms(rooms);
        a.setRent(rent);
        a.setDeposit(deposit);
        a.setStatus(status != null ? status : "Vacant");
        a.setTenantName(tenantName);
        if (imageFile != null && !imageFile.isEmpty()) {
            a.setImageUrl(saveImage(imageFile));
        }
        apartmentRepository.save(a);
        return "redirect:/apartments";
    }

    @GetMapping("/apartments/delete/{id}")
    public String deleteApartment(@PathVariable Long id) {
        apartmentRepository.deleteById(id);
        return "redirect:/apartments";
    }

    private String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            String dir = System.getProperty("user.dir") + File.separator + "uploads";
            new File(dir).mkdirs();
            String name = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
            file.transferTo(new File(dir + File.separator + name));
            return "/uploads/" + name;
        } catch (IOException e) {
            return null;
        }
    }
}