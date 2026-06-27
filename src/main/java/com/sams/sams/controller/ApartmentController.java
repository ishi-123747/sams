package com.sams.sams.controller;
import com.sams.sams.model.Apartment;
import com.sams.sams.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Controller
public class ApartmentController {
    @Autowired private ApartmentService apartmentService;

    @GetMapping("/apartments")
    public String apartments(Model model) {
        model.addAttribute("activePage", "apartments");
        List<Apartment> apartments = apartmentService.getAllApartments();
        model.addAttribute("apartments", apartments);
        model.addAttribute("totalCount", apartments.size());
        model.addAttribute("occupiedCount", apartments.stream().filter(a -> "Occupied".equalsIgnoreCase(a.getStatus())).count());
        model.addAttribute("vacantCount", apartments.stream().filter(a -> "Vacant".equalsIgnoreCase(a.getStatus())).count());
        model.addAttribute("maintenanceCount", apartments.stream().filter(a -> "Maintenance".equalsIgnoreCase(a.getStatus())).count());
        return "apartments";
    }

    @PostMapping("/apartments/add")
    public String addApartment(@ModelAttribute Apartment apartment,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        saveImageIfPresent(apartment, imageFile);
        apartmentService.saveApartment(apartment);
        return "redirect:/apartments";
    }

    @PostMapping("/apartments/edit")
    public String editApartment(@ModelAttribute Apartment apartment,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Apartment existing = apartmentService.getApartmentById(apartment.getId());
        if (existing != null) {
            existing.setApartmentName(apartment.getApartmentName());
            existing.setApartmentNumber(apartment.getApartmentNumber());
            existing.setLocation(apartment.getLocation());
            existing.setRooms(apartment.getRooms());
            existing.setRent(apartment.getRent());
            existing.setDeposit(apartment.getDeposit());
            existing.setStatus(apartment.getStatus());
            existing.setTenantName(apartment.getTenantName());
            if (imageFile != null && !imageFile.isEmpty()) saveImageIfPresent(existing, imageFile);
            apartmentService.saveApartment(existing);
        }
        return "redirect:/apartments";
    }

    @PostMapping("/apartments/delete/{id}")
    public String deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return "redirect:/apartments";
    }

    private void saveImageIfPresent(Apartment apartment, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(System.getProperty("user.dir") + File.separator + "uploads");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Files.copy(imageFile.getInputStream(), uploadPath.resolve(filename));
                apartment.setImageUrl("/uploads/" + filename);
            } catch (IOException ignored) {}
        }
    }
}