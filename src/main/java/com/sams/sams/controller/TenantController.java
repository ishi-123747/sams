package com.sams.sams.controller;
import com.sams.sams.model.Tenant;
import com.sams.sams.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class TenantController {
    @Autowired private TenantService tenantService;

    @GetMapping("/tenants")
    public String tenants(Model model) {
        model.addAttribute("activePage", "tenants");
        List<Tenant> tenants = tenantService.getAllTenants();
        model.addAttribute("tenants", tenants);
        model.addAttribute("totalCount", tenants.size());
        model.addAttribute("activeCount", tenants.stream().filter(t -> "Active".equalsIgnoreCase(t.getStatus())).count());
        model.addAttribute("inactiveCount", tenants.stream().filter(t -> "Inactive".equalsIgnoreCase(t.getStatus())).count());
        return "tenants";
    }

    @PostMapping("/tenants/add")
    public String addTenant(@ModelAttribute Tenant tenant) {
        tenantService.saveTenant(tenant);
        return "redirect:/tenants";
    }

    @PostMapping("/tenants/edit")
    public String editTenant(@ModelAttribute Tenant tenant) {
        Tenant existing = tenantService.getTenantById(tenant.getId());
        if (existing != null) {
            existing.setName(tenant.getName());
            existing.setEmail(tenant.getEmail());
            existing.setPhone(tenant.getPhone());
            existing.setAadhar(tenant.getAadhar());
            existing.setApartmentNumber(tenant.getApartmentNumber());
            existing.setJoinDate(tenant.getJoinDate());
            existing.setStatus(tenant.getStatus());
            existing.setDateOfBirth(tenant.getDateOfBirth());
            tenantService.saveTenant(existing);
        }
        return "redirect:/tenants";
    }

    @PostMapping("/tenants/delete/{id}")
    public String deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return "redirect:/tenants";
    }
}