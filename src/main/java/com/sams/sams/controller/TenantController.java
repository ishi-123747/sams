package com.sams.sams.controller;

import com.sams.sams.model.Tenant;
import com.sams.sams.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TenantController {

    @Autowired
    private TenantRepository tenantRepository;

    @GetMapping("/tenants")
    public String tenants(Model model) {
        List<Tenant> tenants = tenantRepository.findAll();
        model.addAttribute("tenants", tenants);
        model.addAttribute("total",    tenants.size());
        model.addAttribute("active",   tenants.stream().filter(t -> "Active".equalsIgnoreCase(t.getStatus())).count());
        model.addAttribute("pending",  tenants.stream().filter(t -> "Pending".equalsIgnoreCase(t.getStatus())).count());
        model.addAttribute("overdue",  tenants.stream().filter(t -> "Overdue".equalsIgnoreCase(t.getStatus())).count());
        return "tenants";
    }

    @PostMapping("/tenants/add")
    public String addTenant(@RequestParam String name,
                            @RequestParam(required=false) String email,
                            @RequestParam(required=false) String phone,
                            @RequestParam(required=false) String aadhar,
                            @RequestParam(required=false) String apartmentNumber,
                            @RequestParam(required=false) String joinDate,
                            @RequestParam(required=false) String status) {
        Tenant t = new Tenant();
        t.setName(name);
        t.setEmail(email);
        t.setPhone(phone);
        t.setAadhar(aadhar);
        t.setApartmentNumber(apartmentNumber);
        t.setJoinDate(joinDate);
        t.setStatus(status != null ? status : "Active");
        tenantRepository.save(t);
        return "redirect:/tenants";
    }

    @PostMapping("/tenants/edit")
    public String editTenant(@RequestParam Long id,
                             @RequestParam String name,
                             @RequestParam(required=false) String email,
                             @RequestParam(required=false) String phone,
                             @RequestParam(required=false) String aadhar,
                             @RequestParam(required=false) String apartmentNumber,
                             @RequestParam(required=false) String joinDate,
                             @RequestParam(required=false) String status) {
        Tenant t = tenantRepository.findById(id).orElse(new Tenant());
        t.setId(id);
        t.setName(name);
        t.setEmail(email);
        t.setPhone(phone);
        t.setAadhar(aadhar);
        t.setApartmentNumber(apartmentNumber);
        t.setJoinDate(joinDate);
        t.setStatus(status != null ? status : "Active");
        tenantRepository.save(t);
        return "redirect:/tenants";
    }

    @GetMapping("/tenants/delete/{id}")
    public String deleteTenant(@PathVariable Long id) {
        tenantRepository.deleteById(id);
        return "redirect:/tenants";
    }
}