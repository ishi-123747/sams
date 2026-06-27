package com.sams.sams.controller;

import com.sams.sams.model.AuditLog;
import com.sams.sams.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@Controller
public class AuditController {

    @Autowired private AuditLogRepository auditLogRepository;

    @GetMapping("/audit-log")
    public String auditLog(Model model) {
        try {
            List<AuditLog> logs = new ArrayList<>(auditLogRepository.findAll());
            java.util.Collections.reverse(logs);
            model.addAttribute("logs", logs);
        } catch (Exception e) {
            model.addAttribute("logs", new ArrayList<>());
        }
        model.addAttribute("activePage", "audit");
        return "audit-log";
    }

    @PostMapping("/audit-log/clear")
    public String clear() {
        try {
            auditLogRepository.deleteAll();
        } catch (Exception ignored) {}
        return "redirect:/audit-log";
    }
}