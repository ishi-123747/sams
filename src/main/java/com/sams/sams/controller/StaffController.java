package com.sams.sams.controller;

import com.sams.sams.model.Staff;
import com.sams.sams.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StaffController {

    @Autowired private StaffRepository staffRepository;

    @GetMapping("/staff")
    public String staff(Model model) {
        List<Staff> all = staffRepository.findAll();
        model.addAttribute("staffList", all);
        model.addAttribute("totalCount", all.size());
        model.addAttribute("activeCount", staffRepository.countByStatus("Active"));
        model.addAttribute("watchmanCount", staffRepository.countByRole("Watchman"));
        model.addAttribute("cleanerCount", staffRepository.countByRole("Cleaner"));
        model.addAttribute("activePage", "staff-page");
        return "staff";
    }

    @PostMapping("/staff/add")
    public String addStaff(@ModelAttribute Staff staff) {
        if (staff.getStatus() == null || staff.getStatus().isEmpty())
            staff.setStatus("Active");
        staffRepository.save(staff);
        return "redirect:/staff";
    }

    @PostMapping("/staff/edit")
    public String editStaff(@ModelAttribute Staff staff) {
        Staff existing = staffRepository.findById(staff.getId()).orElse(null);
        if (existing != null) {
            existing.setName(staff.getName());
            existing.setRole(staff.getRole());
            existing.setPhone(staff.getPhone());
            existing.setEmail(staff.getEmail());
            existing.setShift(staff.getShift());
            existing.setJoiningDate(staff.getJoiningDate());
            existing.setStatus(staff.getStatus());
            existing.setSalary(staff.getSalary());
            existing.setAadhar(staff.getAadhar());
            existing.setAddress(staff.getAddress());
            staffRepository.save(existing);
        }
        return "redirect:/staff";
    }

    @PostMapping("/staff/delete/{id}")
    public String deleteStaff(@PathVariable Long id) {
        staffRepository.deleteById(id);
        return "redirect:/staff";
    }
}