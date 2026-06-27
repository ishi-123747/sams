package com.sams.sams.controller;
import com.sams.sams.model.Complaint;
import com.sams.sams.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/complaints")
public class ComplaintController {
    @Autowired private ComplaintService complaintService;
    private static final List<String> ALLOWED_REDIRECTS = Arrays.asList("/complaints", "/my-portal");

    @GetMapping
    public String complaints(Model model) {
        model.addAttribute("activePage", "complaints");
        List<Complaint> all = complaintService.getAllComplaints();
        model.addAttribute("complaints", all);
        model.addAttribute("totalCount", all.size());
        model.addAttribute("openCount", all.stream().filter(c -> "Open".equalsIgnoreCase(c.getStatus())).count());
        model.addAttribute("inProgressCount", all.stream().filter(c -> "In Progress".equalsIgnoreCase(c.getStatus())).count());
        model.addAttribute("resolvedCount", all.stream().filter(c -> "Resolved".equalsIgnoreCase(c.getStatus())).count());
        return "complaints";
    }

    @PostMapping("/add")
    public String addComplaint(@ModelAttribute Complaint complaint,
                               @RequestParam(value = "redirect", required = false, defaultValue = "/complaints") String redirect) {
        complaintService.saveComplaint(complaint);
        return "redirect:" + (ALLOWED_REDIRECTS.contains(redirect) ? redirect : "/complaints");
    }

    @PostMapping("/edit")
    public String editComplaint(@ModelAttribute Complaint complaint) {
        Complaint existing = complaintService.getComplaintById(complaint.getId());
        if (existing != null) {
            existing.setTenantName(complaint.getTenantName());
            existing.setApartmentNumber(complaint.getApartmentNumber());
            existing.setSubject(complaint.getSubject());
            existing.setDescription(complaint.getDescription());
            existing.setCategory(complaint.getCategory());
            existing.setPriority(complaint.getPriority());
            complaintService.saveComplaint(existing);
        }
        return "redirect:/complaints";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        complaintService.updateStatus(id, status); return "redirect:/complaints";
    }

    @PostMapping("/delete/{id}")
    public String deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id); return "redirect:/complaints";
    }
}