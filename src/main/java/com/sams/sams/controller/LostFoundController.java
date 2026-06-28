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
import java.util.stream.Collectors;

@Controller
public class LostFoundController {

    @Autowired private LostFoundRepository lostFoundRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private UserRepository userRepository;

    private Tenant findTenant(String username) {
        try {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null && user.getTenantId() != null)
                return tenantRepository.findById(user.getTenantId()).orElse(null);

            Tenant t = tenantRepository.findAll().stream()
                    .filter(x -> x.getName() != null && x.getName().equalsIgnoreCase(username))
                    .findFirst().orElse(null);
            if (t != null) return t;

            t = tenantRepository.findAll().stream()
                    .filter(x -> x.getPhone() != null && x.getPhone().equalsIgnoreCase(username))
                    .findFirst().orElse(null);
            if (t != null) return t;

            return tenantRepository.findAll().stream()
                    .filter(x -> x.getEmail() != null && x.getEmail().equalsIgnoreCase(username))
                    .findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/lost-found")
    public String lostFound(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        List<LostFound> all = lostFoundRepository.findAll();

        List<LostFound> lostItems = all.stream()
                .filter(i -> "Lost".equalsIgnoreCase(i.getType()))
                .collect(Collectors.toList());

        List<LostFound> foundItems = all.stream()
                .filter(i -> "Found".equalsIgnoreCase(i.getType()))
                .collect(Collectors.toList());

        long resolvedCount = all.stream()
                .filter(i -> "Resolved".equalsIgnoreCase(i.getStatus()))
                .count();

        model.addAttribute("items", all);
        model.addAttribute("lostItems", lostItems);
        model.addAttribute("foundItems", foundItems);
        model.addAttribute("lostCount", lostItems.size());
        model.addAttribute("foundCount", foundItems.size());
        model.addAttribute("resolvedCount", resolvedCount);
        model.addAttribute("activePage", "lostfound-page");

        String role = (String) session.getAttribute("role");
        if ("TENANT".equalsIgnoreCase(role)) {
            model.addAttribute("tenant", findTenant(username));
        }

        return "lost-found";
    }

    @PostMapping("/lost-found/add")
    public String addItem(@RequestParam String type,
                          @RequestParam String itemName,
                          @RequestParam(required = false) String description,
                          @RequestParam String location,
                          @RequestParam String contactPhone,
                          HttpSession session) {
        String username = (String) session.getAttribute("username");
        Tenant tenant = findTenant(username);

        LostFound item = new LostFound();
        item.setType(type);
        item.setItemName(itemName);
        item.setDescription(description != null ? description : "");
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