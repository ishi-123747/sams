package com.sams.sams.controller;

import com.sams.sams.model.LeaseAgreement;
import com.sams.sams.repository.LeaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Controller
public class LeaseController {

    @Autowired private LeaseRepository leaseRepository;

    @GetMapping("/leases")
    public String leases(Model model) {
        List<LeaseAgreement> list = leaseRepository.findAll();
        model.addAttribute("leases", list);
        model.addAttribute("totalCount", list.size());
        model.addAttribute("activeCount", leaseRepository.countByStatus("Active"));
        model.addAttribute("expiredCount", leaseRepository.countByStatus("Expired"));
        model.addAttribute("activePage", "leases");
        return "leases";
    }

    @PostMapping("/leases/add")
    public String add(@RequestParam String tenantName,
                      @RequestParam String apartmentNumber,
                      @RequestParam String startDate,
                      @RequestParam String endDate,
                      @RequestParam double rentAmount,
                      @RequestParam double depositAmount,
                      @RequestParam String status,
                      @RequestParam(required = false) MultipartFile agreementFile) {
        LeaseAgreement lease = new LeaseAgreement();
        lease.setTenantName(tenantName);
        lease.setApartmentNumber(apartmentNumber);
        lease.setStartDate(startDate);
        lease.setEndDate(endDate);
        lease.setRentAmount(rentAmount);
        lease.setDepositAmount(depositAmount);
        lease.setStatus(status);
        lease.setUploadedDate(LocalDate.now().toString());

        if (agreementFile != null && !agreementFile.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "leases";
                Files.createDirectories(Paths.get(uploadDir));
                String fileName = System.currentTimeMillis() + "_" + agreementFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                agreementFile.transferTo(filePath.toFile());
                lease.setFilePath("/uploads/leases/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        leaseRepository.save(lease);
        return "redirect:/leases";
    }

    @PostMapping("/leases/delete/{id}")
    public String delete(@PathVariable Long id) {
        leaseRepository.deleteById(id);
        return "redirect:/leases";
    }
}