package com.sams.sams.controller;

import com.sams.sams.model.LostFound;
import com.sams.sams.model.Tenant;
import com.sams.sams.model.User;
import com.sams.sams.repository.LostFoundRepository;
import com.sams.sams.repository.TenantRepository;
import com.sams.sams.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class LostFoundController {

    @Autowired private LostFoundRepository lostFoundRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping("/lost-found")
    public String lostFound(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        List<LostFound> all = lostFoundRepository.findAll();
        model.addAttribute("items", all);
        model.addAttribute("lostItems", lostFoundRepository.findByType("Lost"));
        model.addAttribute("foundItems", lostFoundRepository.findByType("Found"));
        model.addAttribute("lostCount", lostFoundRepository.countByType("Lost"));
        model.addAttribute("foundCount", lostFoundRepository.countByType("Found"));
        model.addAttribute("resolvedCount", lostFoundRepository.countByStatus("Resolved"));
        model.addAttribute("activePage", "lostfound-page");

        // Pre-fill tenant info if tenant
        String role = (String) session.getAttribute("role");
        if ("TENANT".equalsIgnoreCase(role)) {
            User user = userRepository.findByUsername(username).orElse(null);
            Tenant tenant = null;
            if (user != null && user.getTenantId() != null)
                tenant = tenantRepository.findById(user.getTenantId()).orElse(null);
            model.addAttribute("tenant", tenant);
        }

        return "lost-found";
    }

    @PostMapping("/lost-found/add")
    public String addItem(@RequestParam String type,
                          @RequestParam String itemName,
                          @RequestParam String description,
                          @RequestParam String location,
                          @RequestParam String contactPhone,
                          HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElse(null);
        Tenant tenant = null;
        if (user != null && user.getTenantId() != null)
            tenant = tenantRepository.findById(user.getTenantId()).orElse(null);

        LostFound item = new LostFound();
        item.setType(type);
        item.setItemName(itemName);
        item.setDescription(description);
        item.setLocation(location);
        item.setContactPhone(contactPhone);
        item.setReportedBy(tenant != null ? tenant.getName() : username);
        item.setApartmentNumber(tenant != null ? tenant.getApartmentNumber() : "—");
        item.setReportedDate(LocalDate.now().toString());
        item.setStatus("Open");
        lostFoundRepository.save(item);

        return "redirect:/lost-found";
    }

    @PostMapping("/lost-found/resolve/{id}")
    public String resolveItem(@PathVariable Long id) {
        LostFound item = lostFoundRepository.findById(id).orElse(null);
        if (item != null) {
            item.setStatus("Resolved");
            lostFoundRepository.save(item);
        }
        return "redirect:/lost-found";
    }

    @PostMapping("/lost-found/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        lostFoundRepository.deleteById(id);
        return "redirect:/lost-found";
    }
}