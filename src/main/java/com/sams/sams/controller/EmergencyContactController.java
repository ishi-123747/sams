package com.sams.sams.controller;

import com.sams.sams.model.EmergencyContact;
import com.sams.sams.repository.EmergencyContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmergencyContactController {

    @Autowired private EmergencyContactRepository emergencyContactRepository;

    // Both admin and tenant can view
    @GetMapping("/emergency-contacts")
    public String emergencyContacts(Model model) {
        List<EmergencyContact> all = emergencyContactRepository.findAll();
        model.addAttribute("contacts", all);
        model.addAttribute("totalCount", all.size());
        model.addAttribute("fireContacts", emergencyContactRepository.findByCategory("Fire"));
        model.addAttribute("medicalContacts", emergencyContactRepository.findByCategory("Medical"));
        model.addAttribute("policeContacts", emergencyContactRepository.findByCategory("Police"));
        model.addAttribute("utilityContacts", emergencyContactRepository.findByCategory("Utility"));
        model.addAttribute("activePage", "emergency");
        return "emergency-contacts";
    }

    @PostMapping("/emergency-contacts/add")
    public String addContact(@ModelAttribute EmergencyContact contact) {
        emergencyContactRepository.save(contact);
        return "redirect:/emergency-contacts";
    }

    @PostMapping("/emergency-contacts/edit")
    public String editContact(@ModelAttribute EmergencyContact contact) {
        EmergencyContact existing = emergencyContactRepository.findById(contact.getId()).orElse(null);
        if (existing != null) {
            existing.setName(contact.getName());
            existing.setCategory(contact.getCategory());
            existing.setPhone(contact.getPhone());
            existing.setAltPhone(contact.getAltPhone());
            existing.setDescription(contact.getDescription());
            existing.setAvailability(contact.getAvailability());
            existing.setPriority(contact.getPriority());
            emergencyContactRepository.save(existing);
        }
        return "redirect:/emergency-contacts";
    }

    @PostMapping("/emergency-contacts/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        emergencyContactRepository.deleteById(id);
        return "redirect:/emergency-contacts";
    }
}