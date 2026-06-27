package com.sams.sams.controller;

import com.sams.sams.model.MaintenanceRequest;
import com.sams.sams.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
public class MaintenanceController {

    @Autowired private MaintenanceRepository maintenanceRepository;

    @GetMapping("/maintenance")
    public String maintenance(Model model) {
        List<MaintenanceRequest> list = maintenanceRepository.findAll();
        model.addAttribute("requests", list);
        model.addAttribute("totalCount", list.size());
        model.addAttribute("openCount", maintenanceRepository.countByStatus("Open"));
        model.addAttribute("inProgressCount", maintenanceRepository.countByStatus("In Progress"));
        model.addAttribute("resolvedCount", maintenanceRepository.countByStatus("Resolved"));
        model.addAttribute("activePage", "maintenance");
        return "maintenance";
    }

    @PostMapping("/maintenance/add")
    public String add(@RequestParam String title,
                      @RequestParam String description,
                      @RequestParam String apartmentNumber,
                      @RequestParam String tenantName,
                      @RequestParam String priority) {
        MaintenanceRequest r = new MaintenanceRequest();
        r.setTitle(title);
        r.setDescription(description);
        r.setApartmentNumber(apartmentNumber);
        r.setTenantName(tenantName);
        r.setPriority(priority);
        r.setStatus("Open");
        r.setCreatedDate(LocalDate.now().toString());
        maintenanceRepository.save(r);
        return "redirect:/maintenance";
    }

    @PostMapping("/maintenance/update-status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        maintenanceRepository.findById(id).ifPresent(r -> {
            r.setStatus(status);
            if ("Resolved".equals(status)) {
                r.setResolvedDate(LocalDate.now().toString());
            }
            maintenanceRepository.save(r);
        });
        return "redirect:/maintenance";
    }

    @PostMapping("/maintenance/assign/{id}")
    public String assign(@PathVariable Long id, @RequestParam String assignedTo) {
        maintenanceRepository.findById(id).ifPresent(r -> {
            r.setAssignedTo(assignedTo);
            r.setStatus("In Progress");
            maintenanceRepository.save(r);
        });
        return "redirect:/maintenance";
    }

    @PostMapping("/maintenance/delete/{id}")
    public String delete(@PathVariable Long id) {
        maintenanceRepository.deleteById(id);
        return "redirect:/maintenance";
    }
}